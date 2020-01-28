import { connectRouter, RouterState } from 'connected-react-router';
import { History } from 'history';
import { Action, AnyAction, combineReducers, Dispatch } from 'redux';
import { ThunkAction } from 'redux-thunk';

import { ISidebarState, sidebarReducer } from 'core/store/sidebar';
import ServiceFactory from 'services/ServiceFactory';

import {
  IArtifactManagerState,
  artifactManagerReducer,
} from './artifactManager';
import { commentsReducer, ICommentsState } from './comments';
import { compareModelsReducer, ICompareEntitiesState } from './compareEntities';
import {
  dashboardConfigReducer,
  IDashboardConfigState,
} from './dashboardConfig';
import { IDatasetsState, datasetsReducer } from './datasets';
import {
  IDatasetVersionsState,
  datasetVersionsReducer,
} from './datasetVersions';
import {
  IDescriptionActionState,
  descriptionActionReducer,
} from './descriptionAction';
import { experimentRunsReducer, IExperimentRunsState } from './experimentRuns';
import { IExperimentsState, experimentsReducer } from './experiments';
import { filtersReducer, IFilterState } from './filter';
import {
  IProjectCreationState,
  projectCreationReducer,
} from './projectCreation';
import { IProjectsState, projectsReducer } from './projects';
import { IProjectsPageState, projectsPageReducer } from './projectsPage';
import { ITagActionState, tagActionReducer } from './tagAction';

export interface IApplicationState {
  experiments: IExperimentsState;
  comments: ICommentsState;
  compareEntities: ICompareEntitiesState;
  dashboardConfig: IDashboardConfigState;
  experimentRuns: IExperimentRunsState;
  projectCreation: IProjectCreationState;
  projects: IProjectsState;
  projectsPage: IProjectsPageState;
  router: RouterState;
  filters: IFilterState;
  tagAction: ITagActionState;
  descriptionAction: IDescriptionActionState;
  artifactManager: IArtifactManagerState;
  datasets: IDatasetsState;
  datasetVersions: IDatasetVersionsState;
  sidebar: ISidebarState;
}

// Additional props for connected React components. This prop is passed by default with `connect()`
export interface IConnectedReduxProps<A extends Action = any> {
  dispatch: Dispatch<A>;
}

export const createRootReducer = (history: History) =>
  combineReducers<IApplicationState>({
    sidebar: sidebarReducer,
    experiments: experimentsReducer,
    comments: commentsReducer,
    compareEntities: compareModelsReducer,
    dashboardConfig: dashboardConfigReducer,
    experimentRuns: experimentRunsReducer,
    filters: filtersReducer,
    projectCreation: projectCreationReducer,
    projects: projectsReducer,
    projectsPage: projectsPageReducer,
    router: connectRouter(history),
    tagAction: tagActionReducer,
    descriptionAction: descriptionActionReducer,
    artifactManager: artifactManagerReducer,
    datasets: datasetsReducer,
    datasetVersions: datasetVersionsReducer,
  });

export interface IThunkActionDependencies {
  ServiceFactory: typeof ServiceFactory;
  history: History;
}

export type ActionResult<R = void, A extends Action = AnyAction> = ThunkAction<
  R,
  IApplicationState,
  IThunkActionDependencies,
  A
>;
