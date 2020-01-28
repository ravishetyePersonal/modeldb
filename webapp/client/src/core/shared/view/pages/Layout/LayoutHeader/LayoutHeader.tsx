import * as React from 'react';
import { Link } from 'react-router-dom';

import routes from 'routes';

import logo from './images/Verta_logo.svg';
import styles from './LayoutHeader.module.css';

interface ILocalProps {
  userBar: React.ReactNode;
}

class AuthorizedLayoutHeader extends React.PureComponent<ILocalProps> {
  public render() {
    const { userBar } = this.props;
    return (
      <header className={styles.header}>
        <div className={styles.logo}>
          <Link to={routes.index.getRedirectPath({})}>
            <img src={logo} />
          </Link>
        </div>
        {userBar && <div className={styles.userBar}>{userBar}</div>}
      </header>
    );
  }
}

export default AuthorizedLayoutHeader;
