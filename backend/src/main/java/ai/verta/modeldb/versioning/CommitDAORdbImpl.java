package ai.verta.modeldb.versioning;

import ai.verta.modeldb.App;
import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.entities.versioning.CommitEntity;
import ai.verta.modeldb.entities.versioning.InternalFolderElementEntity;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

public class CommitDAORdbImpl implements CommitDAO {
  private static final Logger LOGGER = LogManager.getLogger(CommitDAORdbImpl.class);

  /**
   * commit : details of the commit and the blobs to be added setBlobs : recursively creates trees
   * and blobs in top down fashion and generates SHAs in bottom up fashion getRepository : fetches
   * the repository the commit is made on
   */
  public Response setCommit(Commit commit, BlobFunction setBlobs, RepositoryFunction getRepository)
      throws ModelDBException, NoSuchAlgorithmException {
    try (Session session = ModelDBHibernateUtil.getSessionFactory().openSession()) {
      session.beginTransaction();
      final String rootSha = setBlobs.apply(session);
      long timeCreated = new Date().getTime();
      if (App.getInstance().getStoreClientCreationTimestamp() && commit.getDateCreated() != 0L) {
        timeCreated = commit.getDateCreated();
      }
      final String commitSha = generateCommitSHA(rootSha, commit, timeCreated);
      Commit internalCommit =
          Commit.newBuilder()
              .setDateCreated(timeCreated)
              .setAuthor(commit.getAuthor())
              .setMessage(commit.getMessage())
              .setCommitSha(commitSha)
              .build();
      CommitEntity commitEntity =
          new CommitEntity(
              getRepository.apply(session),
              getCommits(session, commit.getParentShasList()),
              internalCommit, rootSha);
      session.saveOrUpdate(commitEntity);
      session.getTransaction().commit();
      return Response.newBuilder().setCommit(commitEntity.toCommitProto()).build();
    }
  }

  private String generateCommitSHA(String blobSHA, Commit commit, long timeCreated)
      throws NoSuchAlgorithmException {

    StringBuilder sb = new StringBuilder();
    if (!commit.getParentShasList().isEmpty()) {
      List<String> parentSHAs = commit.getParentShasList();
      parentSHAs = parentSHAs.stream().sorted().collect(Collectors.toList());
      sb.append("parent:");
      parentSHAs.forEach(pSHA -> sb.append(pSHA));
    }
    sb.append(":message:")
        .append(commit.getMessage())
        .append(":date_created:")
        .append(timeCreated)
        .append(":author:")
        .append(commit.getAuthor())
        .append(":rootHash:")
        .append(blobSHA);

    return FileHasher.getSha(sb.toString());
  }

  /**
   * 
   * @param session
   * @param ShasList : a list of sha for which the function returns commits
   * @return
   * @throws ModelDBException : if any of the input sha are not identified as a commit
   */
  private List<CommitEntity> getCommits(Session session, ProtocolStringList ShasList)
      throws ModelDBException {
    List<CommitEntity> result =
        ShasList.stream()
            .map(sha -> session.get(CommitEntity.class, sha))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    if (result.size() != ShasList.size()) {
      throw new ModelDBException("Cannot find commits", Code.INVALID_ARGUMENT);
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
