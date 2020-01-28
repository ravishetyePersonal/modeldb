import React from 'react';
import { Omit, connect } from 'react-redux';
import { RouteComponentProps, withRouter } from 'react-router';

import { defaultQuickFilters } from 'core/shared/models/Filters';
import routes, { GetRouteParams } from 'routes';
import {
  loadExperimentRuns,
  resetPagination,
  getExperimentRunsOptions,
} from 'store/experimentRuns';
import { IFilterContext } from 'store/filter';
import { IConnectedReduxProps } from 'store/store';

import ProjectsPagesLayout from '../../../shared/ProjectsPagesLayout/ProjectsPagesLayout';
import makeExprRunsFilterContextName from '../makeExprRunsFilterContextName';
import ProjectPageTabs, {
  IProjectPageTabsLocalProps,
} from '../ProjectPageTabs/ProjectPageTabs';

import styles from './LayoutWithExprRunsFilter.module.css';

interface ILocalProps {
  children: Exclude<React.ReactNode, null | undefined>;
  tabsSetting?: Omit<IProjectPageTabsLocalProps, 'projectId'>;
}

type IUrlProps = GetRouteParams<typeof routes.experimentRuns>;
type AllProps = RouteComponentProps<IUrlProps> &
  ILocalProps &
  IConnectedReduxProps;

interface ILocalState {
  isNeedResetPagination: boolean;
}

class ProjectDetailsPage extends React.Component<AllProps, ILocalState> {
  public state: ILocalState = {
    isNeedResetPagination: false,
  };

  private filterContext: IFilterContext;

  constructor(props: AllProps) {
    super(props);
    const projectId = props.match.params.projectId;
    this.filterContext = {
      quickFilters: [defaultQuickFilters.name, defaultQuickFilters.tag],
      name: makeExprRunsFilterContextName(projectId),
      onApplyFilters: (filters, dispatch) => {
        if (
          Boolean(routes.experimentRuns.getMatch(window.location.pathname)) &&
          this.state.isNeedResetPagination
        ) {
          dispatch(resetPagination(projectId));
        }

        dispatch(loadExperimentRuns(projectId, filters));

        if (!this.state.isNeedResetPagination) {
          this.setState({ isNeedResetPagination: true });
        }
      },
    };
    this.props.dispatch(getExperimentRunsOptions(projectId));
  }

  public render() {
    return (
      <ProjectsPagesLayout
        filterBarSettings={{
          placeholderText: 'Drag and drop parameters and tags here',
          context: this.filterContext,
          withFilterIdsSection: true,
        }}
      >
        <div className={styles.root}>
          <ProjectPageTabs
            {...this.props.tabsSetting || {}}
            projectId={this.props.match.params.projectId}
          />
          <div className={styles.content}>{this.props.children}</div>
        </div>
      </ProjectsPagesLayout>
    );
  }
}

export default withRouter(connect()(ProjectDetailsPage));
