import { IFilterData } from 'core/shared/models/Filters';
import { IPagination } from 'core/shared/models/Pagination';
import { IWorkspace } from 'models/Workspace';
import {
  IServerFiltersInRequest,
  makeAddFiltersToRequest,
} from 'services/serverModel/Filters/converters';
import { addPaginationToRequest } from 'services/serverModel/Pagination/converters';
import {
  addWorkspaceName,
  IServerEntityWithWorkspaceName,
} from 'services/serverModel/Workspace/converters';
import { IServerPaginationInRequest } from 'services/serverModel/Pagination/Pagination';

export type ILoadProjectsRequest = IServerFiltersInRequest &
  IServerPaginationInRequest &
  IServerEntityWithWorkspaceName;
type ITransformedLoadProjectsRequest = Partial<ILoadProjectsRequest>;

const addFilters = makeAddFiltersToRequest<ITransformedLoadProjectsRequest>();

const makeLoadProjectsRequest = (
  filters: IFilterData[],
  pagination?: IPagination,
  workspaceName?: IWorkspace['name']
): Promise<ILoadProjectsRequest> => {
  return Promise.resolve({})
    .then(
      pagination
        ? addPaginationToRequest<ITransformedLoadProjectsRequest>(pagination)
        : pagination
    )
    .then(workspaceName ? addWorkspaceName(workspaceName) : request => request)
    .then(addFilters(filters)) as Promise<ILoadProjectsRequest>;
};

export default makeLoadProjectsRequest;
