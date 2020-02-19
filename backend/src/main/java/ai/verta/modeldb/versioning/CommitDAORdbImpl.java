package ai.verta.modeldb.versioning;

import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.entities.versioning.CommitEntity;
import ai.verta.modeldb.utils.ModelDBHibernateUtil;
import ai.verta.modeldb.versioning.CreateCommitRequest.Response;
import com.google.protobuf.ProtocolStringList;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;

public class CommitDAORdbImpl implements CommitDAO {
  public Response setCommit(
      Commit commit, BlobFunction setBlobs, RepositoryFunction getRepository)
      throws ModelDBException {
    try (Session session = ModelDBHibernateUtil.getSessionFactory().openSession()) {
      session.beginTransaction();
      InternalCommit internalCommit =
          InternalCommit.newBuilder()
              .setDateCreated(new Date().getTime())
              .setAuthor(commit.getAuthor())
              .setMessage(commit.getAuthor())
              .setFolderSha(setBlobs.apply(session))
              .build();
      CommitEntity commitEntity =
          new CommitEntity(
              getRepository.apply(session),
              getCommits(session, commit.getParentShasList()),
              internalCommit);
      session.saveOrUpdate(commitEntity);
      session.getTransaction().commit();
      return Response.newBuilder().setCommit(commitEntity.toProtoCommit()).build();
    }
  }

  private List<CommitEntity> getCommits(Session session, ProtocolStringList parentShasList) {
    return null;
  }
}
