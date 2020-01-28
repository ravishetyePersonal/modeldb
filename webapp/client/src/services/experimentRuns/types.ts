import { DataWithPagination } from 'core/shared/models/Pagination';
import { IComment } from 'models/Comments';
import ModelRecord from 'models/ModelRecord';

export type ILoadExperimentRunsResult = DataWithPagination<
  ILoadModelRecordResult
>;

export interface ILoadModelRecordResult {
  experimentRun: ModelRecord;
  comments: IComment[];
}

export interface ILazyLoadChartData {
  lazyChartData: ModelRecord[];
  totalCount: number;
}
