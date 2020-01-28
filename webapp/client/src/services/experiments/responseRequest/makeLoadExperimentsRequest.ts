import { IFilterData } from 'core/shared/models/Filters';
import { IPagination } from 'core/shared/models/Pagination';
import {
  makeAddFiltersToRequest,
  IServerFiltersInRequest,
} from 'services/serverModel/Filters/converters';
import { addPaginationToRequest } from 'services/serverModel/Pagination/converters';
import { IServerPaginationInRequest } from 'services/serverModel/Pagination/Pagination';

export type IGetExperimentsRequest = {
  project_id: string;
  experiment_ids: string[];
} & IServerFiltersInRequest &
  IServerPaginationInRequest;

type ITransformedGetExperimentRunsRequest = Partial<IGetExperimentsRequest>;
type TransformGetExperimentRunsRequest = (
  request: ITransformedGetExperimentRunsRequest
) => Promise<ITransformedGetExperimentRunsRequest>;

const addProjectId = (
  projectId: string
): TransformGetExperimentRunsRequest => request =>
  Promise.resolve({
    ...request,
    project_id: projectId,
  });

const addPagination = (
  pagination: IPagination
): TransformGetExperimentRunsRequest => request =>
  Promise.resolve(addPaginationToRequest(pagination)(request));

const addFilters = makeAddFiltersToRequest();

const makeGetExperimentsRequest = (
  projectId: string,
  filters: IFilterData[],
  pagination: IPagination
): Promise<IGetExperimentsRequest> => {
  return Promise.resolve({})
    .then(addProjectId(projectId))
    .then(addPagination(pagination))
    .then(addFilters(filters)) as Promise<IGetExperimentsRequest>;
};

export default makeGetExperimentsRequest;
