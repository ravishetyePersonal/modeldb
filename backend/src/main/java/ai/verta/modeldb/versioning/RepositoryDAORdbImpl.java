package ai.verta.modeldb.versioning;

import ai.verta.modeldb.ModelDBConstants;
import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.WorkspaceTypeEnum.WorkspaceType;
import ai.verta.modeldb.authservice.AuthService;
import ai.verta.modeldb.authservice.RoleService;
import ai.verta.modeldb.dto.WorkspaceDTO;
import ai.verta.modeldb.entities.versioning.CommitEntity;
import ai.verta.modeldb.entities.versioning.RepositoryEntity;
import ai.verta.modeldb.entities.versioning.TagsEntity;
import ai.verta.modeldb.utils.ModelDBHibernateUtil;
import ai.verta.modeldb.versioning.GetRepositoryRequest.Response;
import ai.verta.uac.UserInfo;
import io.grpc.Status.Code;
import io.grpc.StatusRuntimeException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class RepositoryDAORdbImpl implements RepositoryDAO {

  private static final Logger LOGGER = LogManager.getLogger(RepositoryDAORdbImpl.class);
  private final AuthService authService;
  private final RoleService roleService;

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

  private static final String GET_REPOSITORY_COUNT_PREFIX_HQL =
      new StringBuilder("Select count(*) From ")
          .append(RepositoryEntity.class.getSimpleName())
          .append(" ")
          .append(SHORT_NAME)
          .append(" where ")
          .toString();

  private static final String GET_REPOSITORY_PREFIX_HQL =
      new StringBuilder("From ")
          .append(RepositoryEntity.class.getSimpleName())
          .append(" ")
          .append(SHORT_NAME)
          .append(" where ")
          .toString();

  private static final String GET_TAG_HQL =
      new StringBuilder("From ")
          .append(TagsEntity.class.getSimpleName())
          .append(" t ")
          .append(" where ")
          .append(" t.id.")
          .append(ModelDBConstants.REPOSITORY_ID)
          .append(" = :repositoryId ")
          .append(" AND t.id.")
          .append(ModelDBConstants.TAG)
          .append(" = :tag ")
          .toString();
  private static final String GET_TAGS_HQL =
      new StringBuilder("From TagsEntity te where te.id.")
          .append(ModelDBConstants.REPOSITORY_ID)
          .append(" = :repoId ")
          .toString();

  public RepositoryDAORdbImpl(AuthService authService, RoleService roleService) {
    this.authService = authService;
    this.roleService = roleService;
  }

  @Override
  public Response getRepository(GetRepositoryRequest request) throws Exception {
    try (Session session = ModelDBHibernateUtil.getSessionFactory().openSession()) {
      session.beginTransaction();
      RepositoryEntity repository = getRepositoryById(session, request.getId());
      session.getTransaction().commit();
      return Response.newBuilder().setRepository(repository.toProto()).build();
    }
  }

  private WorkspaceDTO verifyAndGetWorkspaceDTO(
      RepositoryIdentification id, boolean shouldCheckNamed) throws ModelDBException {
    WorkspaceDTO workspaceDTO = null;
    String message = null;
    if (id.hasNamedId()) {
      UserInfo userInfo;
      try {
        userInfo = authService.getCurrentLoginUserInfo();
      } catch (StatusRuntimeException e) {
        throw new ModelDBException("Authorization error", e.getStatus().getCode());
      }
      RepositoryNamedIdentification named = id.getNamedId();
      try {
        workspaceDTO =
            roleService.getWorkspaceDTOByWorkspaceName(userInfo, named.getWorkspaceName());
      } catch (StatusRuntimeException e) {
        throw new ModelDBException("Error getting workspace", e.getStatus().getCode());
      }
      if (named.getName().isEmpty() && shouldCheckNamed) {
        message = "Repository name should not be empty";
      }
    }

    if (message != null) {
      throw new ModelDBException(message, Code.INVALID_ARGUMENT);
    }
    return workspaceDTO;
  }

  private WorkspaceDTO verifyAndGetWorkspaceDTO(RepositoryIdentification id)
      throws ModelDBException {
    return verifyAndGetWorkspaceDTO(id, true);
  }

  private RepositoryEntity getRepositoryById(Session session, RepositoryIdentification id)
      throws ModelDBException {
    RepositoryEntity repository;
    if (id.hasNamedId()) {
      WorkspaceDTO workspaceDTO = verifyAndGetWorkspaceDTO(id);
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
  public SetRepository.Response setRepository(SetRepository request, boolean create)
      throws ModelDBException {
    try (Session session = ModelDBHibernateUtil.getSessionFactory().openSession()) {
      RepositoryEntity repository;
      session.beginTransaction();
      if (create) {
        WorkspaceDTO workspaceDTO = verifyAndGetWorkspaceDTO(request.getId());
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
        repository = getRepositoryById(session, request.getId());
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
  public DeleteRepositoryRequest.Response deleteRepository(DeleteRepositoryRequest request)
      throws ModelDBException {
    try (Session session = ModelDBHibernateUtil.getSessionFactory().openSession()) {
      session.beginTransaction();
      RepositoryEntity repository = getRepositoryById(session, request.getRepositoryId());
      session.delete(repository);
      session.getTransaction().commit();
      return DeleteRepositoryRequest.Response.newBuilder().setStatus(true).build();
    }
  }

  @Override
  public ListRepositoriesRequest.Response listRepositories(ListRepositoriesRequest request)
      throws ModelDBException {
    try (Session session = ModelDBHibernateUtil.getSessionFactory().openSession()) {
      WorkspaceDTO workspaceDTO =
          verifyAndGetWorkspaceDTO(
              RepositoryIdentification.newBuilder()
                  .setNamedId(
                      RepositoryNamedIdentification.newBuilder()
                          .setWorkspaceName(request.getWorkspaceName()))
                  .build(),
              false);
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
      ListRepositoriesRequest.Response.Builder builder =
          ListRepositoriesRequest.Response.newBuilder();
      query =
          ModelDBHibernateUtil.getWorkspaceEntityQuery(
              session,
              SHORT_NAME,
              GET_REPOSITORY_COUNT_PREFIX_HQL,
              "repositoryName",
              null,
              ModelDBConstants.WORKSPACE_ID,
              workspaceDTO.getWorkspaceId(),
              workspaceDTO.getWorkspaceType(),
              false);
      list.forEach((o) -> builder.addRepositories(((RepositoryEntity) o).toProto()));
      final Long value = (Long) query.uniqueResult();
      builder.setTotalRecords(value);
      return builder.build();
    } catch (ModelDBException e) {
      LOGGER.warn(e.getMessage(), e);
      throw e;
    }
  }

  @Override
  public SetTagRequest.Response setTag(SetTagRequest request) throws ModelDBException {
    try (Session session = ModelDBHibernateUtil.getSessionFactory().openSession()) {
      RepositoryEntity repository = getRepositoryById(session, request.getRepositoryId());

      // TODO: Check commit_hash exists in DB
      TagsEntity tagsEntity =
          new TagsEntity(repository.getId(), request.getCommitSha(), request.getTag());
      session.saveOrUpdate(tagsEntity);
      return SetTagRequest.Response.newBuilder().build();
    }
  }

  @Override
  public GetTagRequest.Response getTag(GetTagRequest request) throws ModelDBException {
    try (Session session = ModelDBHibernateUtil.getSessionFactory().openSession()) {
      RepositoryEntity repository = getRepositoryById(session, request.getRepositoryId());

      Query query = session.createQuery(GET_TAG_HQL);
      query.setParameter("repositoryId", repository.getId());
      query.setParameter("tag", request.getTag());
      TagsEntity tagsEntity = (TagsEntity) query.uniqueResult();
      CommitEntity commitEntity = session.get(CommitEntity.class, tagsEntity.getCommit_hash());
      return GetTagRequest.Response.newBuilder().setCommit(commitEntity.toCommitProto()).build();
    }
  }

  @Override
  public DeleteTagRequest.Response deleteTag(DeleteTagRequest request) throws ModelDBException {
    try (Session session = ModelDBHibernateUtil.getSessionFactory().openSession()) {
      RepositoryEntity repository = getRepositoryById(session, request.getRepositoryId());
      session.beginTransaction();

      TagsEntity tagsEntity =
          session.load(
              TagsEntity.class, new TagsEntity.TagId(request.getTag(), repository.getId()));
      session.delete(tagsEntity);
      session.getTransaction().commit();
      return DeleteTagRequest.Response.newBuilder().build();
    }
  }

  @Override
  public ListTagsRequest.Response listTags(ListTagsRequest request) throws ModelDBException {
    try (Session session = ModelDBHibernateUtil.getSessionFactory().openSession()) {
      RepositoryEntity repository = getRepositoryById(session, request.getRepositoryId());
      session.beginTransaction();

      Query query = session.createQuery(GET_TAGS_HQL);
      query.setParameter("repoId", repository.getId());
      List<TagsEntity> tagsEntities = query.list();

      session.getTransaction().commit();
      return ListTagsRequest.Response.newBuilder()
          .addAllTags(
              tagsEntities.stream()
                  .map(tagsEntity -> tagsEntity.getId().getTag())
                  .collect(Collectors.toList()))
          .build();
    }
  }
}
