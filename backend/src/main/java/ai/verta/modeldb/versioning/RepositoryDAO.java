package ai.verta.modeldb.versioning;

import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.dto.WorkspaceDTO;
import ai.verta.modeldb.versioning.ListRepositoriesRequest.Response;

interface RepositoryDAO {

  GetRepositoryRequest.Response getRepository(
      GetRepositoryRequest request, WorkspaceDTO workspaceDTO) throws Exception;

  SetRepository.Response setRepository(
      SetRepository request, WorkspaceDTO workspaceDTO, boolean create) throws ModelDBException;

  DeleteRepositoryRequest.Response deleteRepository(
      DeleteRepositoryRequest request, WorkspaceDTO workspaceDTO) throws ModelDBException;

  Response listRepositories(ListRepositoriesRequest request, WorkspaceDTO workspaceDTO);
}
