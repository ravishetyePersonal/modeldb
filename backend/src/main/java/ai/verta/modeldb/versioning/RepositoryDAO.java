package ai.verta.modeldb.versioning;

import ai.verta.modeldb.ModelDBException;

interface RepositoryDAO {

  GetRepositoryRequest.Response getRepository(GetRepositoryRequest request) throws Exception;

  SetRepository.Response setRepository(SetRepository request, boolean create)
      throws ModelDBException;

  DeleteRepositoryRequest.Response deleteRepository(DeleteRepositoryRequest request)
      throws ModelDBException;

  ListRepositoriesRequest.Response listRepositories(ListRepositoriesRequest request)
      throws ModelDBException;

  ListTagsRequest.Response listTags(ListTagsRequest request) throws ModelDBException;

  SetTagRequest.Response setTag(SetTagRequest request) throws ModelDBException;

  GetTagRequest.Response getTag(GetTagRequest request) throws ModelDBException;

  DeleteTagRequest.Response deleteTag(DeleteTagRequest request) throws ModelDBException;
}
