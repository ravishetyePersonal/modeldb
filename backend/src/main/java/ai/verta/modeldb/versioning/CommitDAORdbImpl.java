package ai.verta.modeldb.versioning;

import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.entities.versioning.CommitEntity;
import ai.verta.modeldb.entities.versioning.RepositoryEntity;
import ai.verta.modeldb.utils.ModelDBHibernateUtil;
import ai.verta.modeldb.versioning.CreateCommitRequest.Response;
import com.google.protobuf.ProtocolStringList;
import io.grpc.Status.Code;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.Query;
import org.hibernate.Session;

public class CommitDAORdbImpl implements CommitDAO {
  public Response setCommit(Commit commit, BlobFunction setBlobs, RepositoryFunction getRepository)
      throws ModelDBException, NoSuchAlgorithmException {
    try (Session session = ModelDBHibernateUtil.getSessionFactory().openSession()) {
      session.beginTransaction();
      Commit internalCommit =
          Commit.newBuilder()
              .setDateCreated(new Date().getTime())
              .setAuthor(commit.getAuthor())
              .setMessage(commit.getAuthor())
              .setCommitSha(setBlobs.apply(session))
              .build();
      CommitEntity commitEntity =
          new CommitEntity(
              getRepository.apply(session),
              getCommits(session, commit.getParentShasList()),
              internalCommit);
      session.saveOrUpdate(commitEntity);
      session.getTransaction().commit();
      return Response.newBuilder().setCommit(commitEntity.toCommitProto()).build();
    }
  }

  private List<CommitEntity> getCommits(Session session, ProtocolStringList parentShasList)
      throws ModelDBException {
    List<CommitEntity> result =
        parentShasList.stream()
            .map(sha -> session.get(CommitEntity.class, sha))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    if (result.size() != parentShasList.size()) {
      throw new ModelDBException("Cannot find parent commits", Code.INVALID_ARGUMENT);
    }
    return result;
  }

  private boolean commitRepositoryMappingExists(
      Session session, String commitHash, Long repositoryId) {
    String queryString =
        "SELECT count(*) FROM repository_commit rc WHERE rc.commit_hash = :commitHash AND rc.repository_id = :repoId";
    Query query = session.createQuery(queryString);
    query.setParameter("commitHash", commitHash);
    query.setParameter("repoId", repositoryId);
    Long count = (Long) query.getSingleResult();
    return count > 0;
  }

  @Override
  public Commit getCommit(String commitHash, RepositoryFunction getRepository)
      throws ModelDBException {
    try (Session session = ModelDBHibernateUtil.getSessionFactory().openSession()) {
      session.beginTransaction();
      RepositoryEntity repositoryEntity = getRepository.apply(session);
      boolean exists = commitRepositoryMappingExists(session, commitHash, repositoryEntity.getId());
      if (!exists) {
        throw new ModelDBException(
            "Commit_hash and repository_id mapping not found", Code.NOT_FOUND);
      }

      CommitEntity commitEntity = session.load(CommitEntity.class, commitHash);
      session.getTransaction().commit();
      return commitEntity.toCommitProto();
    }
  }
}
