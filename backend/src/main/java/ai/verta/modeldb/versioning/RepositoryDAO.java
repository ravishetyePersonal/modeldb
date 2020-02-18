package ai.verta.modeldb.versioning;

import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.dto.WorkspaceDTO;
import ai.verta.modeldb.entities.versioning.RepositoryEntity;
import ai.verta.modeldb.versioning.ListRepositoriesRequest.Response;
import org.hibernate.Session;

interface RepositoryDAO {

  GetRepositoryRequest.Response getRepository(
      GetRepositoryRequest request, WorkspaceDTO workspaceDTO) throws Exception;

  RepositoryEntity getRepositoryById(
      Session session, RepositoryIdentification id, WorkspaceDTO workspaceDTO)
      throws ModelDBException;

  SetRepository.Response setRepository(
      SetRepository request, WorkspaceDTO workspaceDTO, boolean create) throws ModelDBException;

  DeleteRepositoryRequest.Response deleteRepository(
      DeleteRepositoryRequest request, WorkspaceDTO workspaceDTO) throws ModelDBException;

  Response listRepositories(ListRepositoriesRequest request, WorkspaceDTO workspaceDTO);
}
