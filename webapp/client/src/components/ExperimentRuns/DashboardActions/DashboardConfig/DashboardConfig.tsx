import { bind } from 'decko';
import * as R from 'ramda';
import * as React from 'react';
import onClickOutside from 'react-onclickoutside';
import { connect } from 'react-redux';

import Checkbox from 'core/shared/view/elements/Checkbox/Checkbox';
import ClickOutsideListener from 'core/shared/view/elements/ClickOutsideListener/ClickOutsideListener';
import { Icon } from 'core/shared/view/elements/Icon/Icon';
import ModelRecord from 'models/ModelRecord';
import {
  IColumnConfig,
  selectColumnConfig,
  toggleColumnVisibility,
} from 'store/dashboardConfig';
import { selectExperimentRuns } from 'store/experimentRuns';
import { IApplicationState, IConnectedReduxProps } from 'store/store';

import styles from './DashboardConfig.module.css';

interface ILocalState {
  isOpened: boolean;
}

interface IPropsFromState {
  columnConfig: IColumnConfig;
  experimentRuns: ModelRecord[] | null;
}

type AllProps = IConnectedReduxProps & IPropsFromState;
class DashboardConfig extends React.Component<AllProps, ILocalState> {
  public state: ILocalState = { isOpened: false };

  public componentDidMount() {
    this.setState({ isOpened: false });
  }

  public render() {
    const { columnConfig } = this.props;
    return (
      <ClickOutsideListener onClickOutside={this.handleClickOutside}>
        <div className={styles.root}>
          <div className={styles.user_bar} onClick={this.toggleMenu}>
            <Icon type="cog" className={styles.dashboard_cog} />
          </div>
          {this.state.isOpened ? (
            <div className={styles.drop_down}>
              <h4 className={styles.title}>Add/Drop Columns</h4>
              <div className={styles.menu_items}>
                {R.sortBy(({ order }) => order, R.values(columnConfig)).map(
                  columnData => (
                    <label className={styles.menu_item} key={columnData.name}>
                      <div className={styles.menu_item_checkbox}>
                        <Checkbox
                          value={columnData.isShown}
                          size="medium"
                          onChange={this.makeColumnsUpdateHandler(
                            columnData.name
                          )}
                        />
                      </div>
                      <div className={styles.menu_item_label}>
                        {columnData.label}
                      </div>
                    </label>
                  )
                )}
              </div>
            </div>
          ) : (
            ''
          )}
        </div>
      </ClickOutsideListener>
    );
  }

  @bind
  private handleClickOutside() {
    this.setState({ isOpened: false });
  }

  @bind
  private makeColumnsUpdateHandler(columnName: keyof IColumnConfig) {
    return () => {
      this.props.dispatch(toggleColumnVisibility({ columnName }));
    };
  }

  @bind
  private toggleMenu() {
    this.setState(prevState => ({ isOpened: !prevState.isOpened }));
  }
}

const mapStateToProps = (state: IApplicationState): IPropsFromState => ({
  columnConfig: selectColumnConfig(state),
  experimentRuns: selectExperimentRuns(state),
});

export default connect(mapStateToProps)(onClickOutside(DashboardConfig));
