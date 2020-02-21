package ai.verta.modeldb.versioning;

import ai.verta.modeldb.ModelDBConstants;
import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.WorkspaceTypeEnum.WorkspaceType;
import ai.verta.modeldb.dto.WorkspaceDTO;
import ai.verta.modeldb.entities.versioning.RepositoryEntity;
import ai.verta.modeldb.utils.ModelDBHibernateUtil;
import ai.verta.modeldb.versioning.GetRepositoryRequest.Response;
import ai.verta.modeldb.versioning.ListRepositoriesRequest.Response.Builder;
import io.grpc.Status.Code;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class RepositoryDAORdbImpl implements RepositoryDAO {

  private static final Logger LOGGER = LogManager.getLogger(RepositoryDAORdbImpl.class);

  private static final String SHORT_NAME = "repo";

  private static final String GET_REPOSITORY_COUNT_BY_NAME_PREFIX_HQL =
      new StringBuilder("Select count(*) From ")
          .append(RepositoryEntity.class.getSimpleName())
          .append(" ")
          .append(SHORT_NAME)
          .append(" where ")
          .append(" ")
          .append(SHORT_NAME)
          .append(".")
          .append(ModelDBConstants.NAME)
          .append(" = :repositoryName ")
          .toString();

  private static final String GET_REPOSITORY_BY_NAME_PREFIX_HQL =
      new StringBuilder("From ")
          .append(RepositoryEntity.class.getSimpleName())
          .append(" ")
          .append(SHORT_NAME)
          .append(" where ")
          .append(" ")
          .append(SHORT_NAME)
          .append(".")
          .append(ModelDBConstants.NAME)
          .append(" = :repositoryName ")
          .toString();

  private static final String GET_REPOSITORY_PREFIX_HQL =
      new StringBuilder("From ")
          .append(RepositoryEntity.class.getSimpleName())
          .append(" ")
          .append(SHORT_NAME)
          .append(" where ")
          .toString();

  @Override
  public Response getRepository(GetRepositoryRequest request, WorkspaceDTO workspaceDTO)
      throws Exception {
    try (Session session = ModelDBHibernateUtil.getSessionFactory().openSession()) {
      session.beginTransaction();
      RepositoryEntity repository = getRepositoryById(session, request.getId(), workspaceDTO);
      session.getTransaction().commit();
      return Response.newBuilder().setRepository(repository.toProto()).build();
    }
  }

  private RepositoryEntity getRepositoryById(
      Session session, RepositoryIdentification id, WorkspaceDTO workspaceDTO)
      throws ModelDBException {
    RepositoryEntity repository;
    if (id.hasNamedId()) {
      repository =
          getRepositoryByName(session, id.getNamedId().getName(), workspaceDTO)
              .orElseThrow(
                  () -> new ModelDBException("Couldn't find repository by name", Code.NOT_FOUND));
    } else {
      repository =
          getRepositoryById(session, id.getRepoId())
              .orElseThrow(
                  () -> new ModelDBException("Couldn't find repository by id", Code.NOT_FOUND));
    }
    return repository;
  }

  private Optional<RepositoryEntity> getRepositoryById(Session session, String id) {
    return getRepositoryById(session, Long.parseLong(id));
  }

  private Optional<RepositoryEntity> getRepositoryById(Session session, long id) {
    return Optional.ofNullable(session.get(RepositoryEntity.class, id));
  }

  private Optional<RepositoryEntity> getRepositoryByName(
      Session session, String name, WorkspaceDTO workspaceDTO) {
    Query query =
        ModelDBHibernateUtil.getWorkspaceEntityQuery(
            session,
            SHORT_NAME,
            GET_REPOSITORY_BY_NAME_PREFIX_HQL,
            "repositoryName",
            name,
            ModelDBConstants.WORKSPACE_ID,
            workspaceDTO.getWorkspaceId(),
            workspaceDTO.getWorkspaceType(),
            true);
    return Optional.ofNullable((RepositoryEntity) query.uniqueResult());
  }

  @Override
  public SetRepository.Response setRepository(
      SetRepository request, WorkspaceDTO workspaceDTO, boolean create) throws ModelDBException {
    try (Session session = ModelDBHibernateUtil.getSessionFactory().openSession()) {
      RepositoryEntity repository;
      session.beginTransaction();
      if (create) {
        ModelDBHibernateUtil.checkIfEntityAlreadyExists(
            session,
            SHORT_NAME,
            GET_REPOSITORY_COUNT_BY_NAME_PREFIX_HQL,
            "Repository",
            "repositoryName",
            request.getRepository().getName(),
            ModelDBConstants.WORKSPACE_ID,
            workspaceDTO.getWorkspaceId(),
            workspaceDTO.getWorkspaceType(),
            LOGGER);
        repository = new RepositoryEntity(request.getRepository().getName(), workspaceDTO);
      } else {
        repository = getRepositoryById(session, request.getId(), workspaceDTO);
        ModelDBHibernateUtil.checkIfEntityAlreadyExists(
            session,
            SHORT_NAME,
            GET_REPOSITORY_COUNT_BY_NAME_PREFIX_HQL,
            "Repository",
            "repositoryName",
            request.getRepository().getName(),
            ModelDBConstants.WORKSPACE_ID,
            repository.getWorkspace_id(),
            WorkspaceType.forNumber(repository.getWorkspace_type()),
            LOGGER);
        repository.update(request);
      }
      session.saveOrUpdate(repository);
      session.getTransaction().commit();
      return SetRepository.Response.newBuilder().setRepository(repository.toProto()).build();
    }
  }

  @Override
  public DeleteRepositoryRequest.Response deleteRepository(
      DeleteRepositoryRequest request, WorkspaceDTO workspaceDTO) throws ModelDBException {
    try (Session session = ModelDBHibernateUtil.getSessionFactory().openSession()) {
      session.beginTransaction();
      RepositoryEntity repository =
          getRepositoryById(session, request.getRepositoryId(), workspaceDTO);
      session.delete(repository);
      session.getTransaction().commit();
      return DeleteRepositoryRequest.Response.newBuilder().setStatus(true).build();
    }
  }

  @Override
  public ListRepositoriesRequest.Response listRepositories(
      ListRepositoriesRequest request, WorkspaceDTO workspaceDTO) {
    try (Session session = ModelDBHibernateUtil.getSessionFactory().openSession()) {
      Query query =
          ModelDBHibernateUtil.getWorkspaceEntityQuery(
              session,
              SHORT_NAME,
              GET_REPOSITORY_PREFIX_HQL,
              "repositoryName",
              null,
              ModelDBConstants.WORKSPACE_ID,
              workspaceDTO.getWorkspaceId(),
              workspaceDTO.getWorkspaceType(),
              false);
      int pageLimit = request.getPagination().getPageLimit();
      query.setFirstResult((request.getPagination().getPageNumber() - 1) * pageLimit);
      query.setMaxResults(pageLimit);
      List list = query.list();
      ListRepositoriesRequest.Response.Builder builder = ListRepositoriesRequest.Response
          .newBuilder();
      list.forEach((o) -> builder.addRepository(((RepositoryEntity) o).toProto()));
      return builder.build();
    }
  }
}
