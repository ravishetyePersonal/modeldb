import { ClickAwayListener } from '@material-ui/core';
import cn from 'classnames';
import { bind } from 'decko';
import * as React from 'react';

import Button from '../Button/Button';
import { Icon } from '../Icon/Icon';
import styles from './SplitButton.module.css';

interface ILocalProps {
  theme: 'blue' | 'red';
  disabled?: boolean;
  isLoading?: boolean;
  primaryAction: IAction;
  dataTest?: string;
  otherActions: [IAction, ...IAction[]];
}

interface IAction {
  label: string;
  dataTest?: string;
  onApply(): void;
}

interface ILocalState {
  isShowOtherActions: boolean;
}

class SplitButton extends React.PureComponent<ILocalProps, ILocalState> {
  public state: ILocalState = { isShowOtherActions: false };

  private rootRefObject = React.createRef<HTMLDivElement>();
  private rootNodeInfo: { width?: number; left?: number } = {
    width: undefined,
    left: undefined,
  };

  public componentDidMount() {
    if (this.rootRefObject.current) {
      this.rootNodeInfo = {
        width: this.rootRefObject.current.offsetWidth,
        left: this.rootRefObject.current.getBoundingClientRect().left,
      };
    }
  }

  public render() {
    const {
      primaryAction,
      otherActions,
      isLoading,
      disabled,
      theme,
    } = this.props;
    const { isShowOtherActions } = this.state;
    return (
      <div className={styles.root} ref={this.rootRefObject}>
        <div className={styles.primaryAction}>
          <Button
            theme={theme === 'blue' ? 'default' : theme}
            dataTest={primaryAction.dataTest}
            isLoading={isLoading}
            disabled={disabled}
            onClick={this.makeOnActionClick(primaryAction)}
          >
            {primaryAction.label}
          </Button>
        </div>
        <div className={styles.toggler}>
          <Button
            theme={theme === 'blue' ? 'default' : theme}
            disabled={disabled || isLoading}
            dataTest={this.props.dataTest || 'split-button'}
            fullWidth={true}
            onClick={this.toggleOtherActions}
          >
            <Icon type="caret-down" />
          </Button>
        </div>
        <ClickAwayListener onClickAway={this.closeOtherActions}>
          <div
            className={cn(styles.otherActions, {
              [styles.opened]: isShowOtherActions,
            })}
          >
            {otherActions.map((action, i) => (
              <div
                key={i}
                className={styles.otherAction}
                onClick={this.makeOnActionClick(action)}
              >
                {action.label}
              </div>
            ))}
          </div>
        </ClickAwayListener>
      </div>
    );
  }

  private makeOnActionClick(action: IAction) {
    return () => {
      action.onApply();
      if (this.state.isShowOtherActions) {
        this.closeOtherActions();
      }
    };
  }

  @bind
  private toggleOtherActions() {
    if (this.state.isShowOtherActions) {
      this.closeOtherActions();
    } else {
      this.showOtherActions();
    }
  }
  @bind
  private showOtherActions() {
    this.setState({ isShowOtherActions: true });
  }
  @bind
  private closeOtherActions() {
    this.setState({ isShowOtherActions: false });
  }
}

const changeTranslate3d = (
  args: { x: string },
  transform3d: string
): string => {
  return transform3d.replace(/translate3d\(.+?,/, `translate3d(${args.x},`);
};

export default SplitButton;
