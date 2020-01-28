import cn from 'classnames';
import { bind } from 'decko';
import * as React from 'react';
import { connect } from 'react-redux';

import {
  selectIsCollapsedSidebar,
  toggleCollapsingSidebar,
} from 'core/store/sidebar';
import { IApplicationState } from 'store/store';

import { IBreadcrumbsBuilder } from './Breadcrumbs/BreadcrumbsBuilder';
import styles from './Layout.module.css';
import AuthorizedLayoutHeader from './LayoutHeader/LayoutHeader';
import Breadcrumbs from './Breadcrumbs/Breadcrumbs';
import Sidebar from './Sidebar/Sidebar';

type ILocalProps = {
  breadcrumbsBuilder?: IBreadcrumbsBuilder;
  children: Exclude<React.ReactNode, null | undefined>;
  additionalSidebar?: React.ReactNode;
  userBar?: React.ReactNode;
} & Pick<React.ComponentProps<typeof Sidebar>, 'filterBarSettings'>;

const mapStateToProps = (state: IApplicationState) => ({
  isCollapsedSidebar: selectIsCollapsedSidebar(state),
});

const actionProps = { toggleCollapsingSidebar };

type AllProps = ILocalProps &
  ReturnType<typeof mapStateToProps> &
  typeof actionProps;

class AuthorizedLayout extends React.Component<AllProps> {
  public render() {
    const {
      filterBarSettings,
      isCollapsedSidebar,
      breadcrumbsBuilder,
      additionalSidebar,
      children,
      userBar,
    } = this.props;
    return (
      <div
        className={cn(styles.layout, {
          [styles.collapsedSidebar]: isCollapsedSidebar,
          [styles.withAdditionalSidebar]: Boolean(additionalSidebar),
        })}
      >
        <div className={styles.header}>
          <AuthorizedLayoutHeader userBar={userBar} />
        </div>
        <div className={styles.content_area}>
          <div className={styles.sidebar}>
            <Sidebar
              filterBarSettings={filterBarSettings}
              isCollapsed={isCollapsedSidebar}
              onToggleCollapsingSidebar={this.onToggleCollapsingSidebar}
            />
          </div>
          {additionalSidebar && (
            <div className={styles.additionalSidebar}>{additionalSidebar}</div>
          )}
          <div className={styles.contentWithBreadcrumbs}>
            {breadcrumbsBuilder && (
              <div className={styles.breadcrumbs}>
                <Breadcrumbs breadcrumbBuilder={breadcrumbsBuilder} />
              </div>
            )}
            <div className={styles.content}>{children}</div>
          </div>
        </div>
      </div>
    );
  }

  @bind
  private onToggleCollapsingSidebar() {
    this.props.toggleCollapsingSidebar();
  }
}

export type IAuthorizedLayoutLocalProps = ILocalProps;
export default connect(
  mapStateToProps,
  actionProps
)(AuthorizedLayout);
