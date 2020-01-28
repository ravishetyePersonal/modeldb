import { IFilterData } from 'core/shared/models/Filters';
import { IPagination } from 'core/shared/models/Pagination';
import { IWorkspace } from 'models/Workspace';
import {
  makeAddFiltersToRequest,
  IServerFiltersInRequest,
} from 'services/serverModel/Filters/converters';
import { addPaginationToRequest } from 'services/serverModel/Pagination/converters';
import {
  IServerEntityWithWorkspaceName,
  addWorkspaceName,
} from 'services/serverModel/Workspace/converters';
import { IServerPaginationInRequest } from 'services/serverModel/Pagination/Pagination';

export type ILoadDatasetsRequest = {
  dataset_ids: string[];
} & IServerPaginationInRequest &
  IServerFiltersInRequest &
  IServerEntityWithWorkspaceName;

type ITransformedLoadDatasetsRequest = Partial<ILoadDatasetsRequest>;
type TransformLoadDatasetsRequest = (
  request: ITransformedLoadDatasetsRequest
) => Promise<ITransformedLoadDatasetsRequest>;

const addPagination = (
  pagination: IPagination
): TransformLoadDatasetsRequest => request =>
  Promise.resolve(addPaginationToRequest(pagination)(request));

const addFilters = makeAddFiltersToRequest();

const makeLoadDatasetsRequest = (
  filters: IFilterData[],
  pagination: IPagination,
  workspaceName?: IWorkspace['name']
): Promise<ILoadDatasetsRequest> => {
  return Promise.resolve({})
    .then(addPagination(pagination))
    .then(workspaceName ? addWorkspaceName(workspaceName) : request => request)
    .then(addFilters(filters)) as Promise<ILoadDatasetsRequest>;
};

export default makeLoadDatasetsRequest;
