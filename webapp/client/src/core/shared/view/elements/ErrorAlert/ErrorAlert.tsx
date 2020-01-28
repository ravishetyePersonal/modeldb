import { bind } from 'decko';
import * as React from 'react';

import { Icon } from '../Icon/Icon';
import styles from './ErrorAlert.module.css';

interface ILocalProps {
  message: string;
  description?: string;
}

interface ILocalState {
  isShown: boolean;
}

class ErrorAlert extends React.PureComponent<ILocalProps, ILocalState> {
  public state: ILocalState = { isShown: true };

  public render() {
    const { message, description } = this.props;
    const { isShown } = this.state;
    return isShown ? (
      <div className={styles.root}>
        <div className={styles.header}>
          <div className={styles.message}>{message}</div>
          <Icon type="close" className={styles.close} onClick={this.onClose} />
        </div>
        {description && <div className={styles.description}>{description}</div>}
      </div>
    ) : null;
  }

  @bind
  private onClose() {
    this.setState({ isShown: false });
  }
}

export default ErrorAlert;
