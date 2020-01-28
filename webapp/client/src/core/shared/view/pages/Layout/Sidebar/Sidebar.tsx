import { bind } from 'decko';
import * as React from 'react';
import { connect } from 'react-redux';

import FilterManager from 'components/Filter/FilterManager/FilterManager';
import { Icon } from 'core/shared/view/elements/Icon/Icon';
import routes from 'routes';
import { IFilterContext } from 'store/filter';
import { IApplicationState } from 'store/store';
import { selectCurrentWorkspaceNameOrDefault } from 'store/workspaces';

import LayoutLink from './LayoutLink/LayoutLink';
import styles from './Sidebar.module.css';

interface ILocalProps {
  isCollapsed: boolean;
  filterBarSettings?: {
    context: IFilterContext;
    placeholderText: string;
    withFilterIdsSection?: boolean;
  };
  onToggleCollapsingSidebar(): void;
}

const mapStateToProps = (state: IApplicationState) => {
  return {
    workspaceName: selectCurrentWorkspaceNameOrDefault(state),
  };
};

type AllProps = ILocalProps & ReturnType<typeof mapStateToProps>;

interface ILocalState {
  height: string;
  marginLeft: string;
}

class Sidebar extends React.PureComponent<AllProps, ILocalState> {
  public state: ILocalState = {
    height: `calc(100% - ${this.getHeaderHeight()})`,
    marginLeft: '0',
  };

  public componentDidMount() {
    window.addEventListener('scroll', this.updateStateOnScroll);
    this.updateStateOnScroll();
  }

  public componentWillUnmount() {
    window.removeEventListener('scroll', this.updateStateOnScroll);
  }

  public render() {
    const {
      filterBarSettings,
      isCollapsed,
      workspaceName,
      onToggleCollapsingSidebar: onToggleCollapsingFilterBar,
    } = this.props;

    return (
      <div className={styles.root} style={this.state}>
        <nav className={styles.mainNavigation}>
          <LayoutLink
            to={routes.projects.getRedirectPath({ workspaceName })}
            iconType="folder"
            isCollapsed={isCollapsed}
          >
            Projects
          </LayoutLink>
          <LayoutLink
            to={routes.datasets.getRedirectPath({ workspaceName })}
            iconType="bookmarks"
            isCollapsed={isCollapsed}
          >
            Datasets
          </LayoutLink>
        </nav>
        {filterBarSettings && (
          <div className={styles.filters}>
            <FilterManager
              {...filterBarSettings}
              isDisabled={false}
              isCollapsed={isCollapsed}
              onExpandSidebar={this.props.onToggleCollapsingSidebar}
            />
          </div>
        )}
        <div className={styles.additionalLinks}>
          <LayoutLink
            isExternal={true}
            to="https://verta.readme.io/docs"
            iconType="help-outline"
            isCollapsed={isCollapsed}
          >
            Docs
          </LayoutLink>
          <LayoutLink
            isExternal={true}
            to="https://www.verta.ai"
            iconType="link"
            isCollapsed={isCollapsed}
          >
            Verta.ai
          </LayoutLink>
        </div>
        <div
          className={styles.collapseSidebarButton}
          onClick={onToggleCollapsingFilterBar}
        >
          <Icon type={isCollapsed ? 'arrow-right' : 'arrow-left'} />
        </div>
      </div>
    );
  }

  @bind
  private updateStateOnScroll() {
    this.updateHeightOnVerticalScroll();
    this.updatePosOnHorizontalScroll();
  }

  @bind
  private updateHeightOnVerticalScroll() {
    const offset = parseInt(this.getHeaderHeight()) - window.scrollY;
    this.setState({
      height: `calc(100% - ${offset < 0 ? 0 : offset}px)`,
    });
  }

  @bind
  private updatePosOnHorizontalScroll() {
    this.setState({
      marginLeft: window.scrollX > 0 ? `-${window.scrollX}px` : '0',
    });
  }

  @bind
  private getHeaderHeight() {
    return getComputedStyle(document.documentElement).getPropertyValue(
      '--header-height'
    );
  }
}

export default connect(mapStateToProps)(Sidebar);
