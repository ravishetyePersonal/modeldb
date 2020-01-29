import React from 'react';
import { RouteComponentProps, withRouter } from 'react-router';

import Pages from 'pages';

type AllProps = RouteComponentProps;

class AppTest extends React.PureComponent<AllProps> {
  public render() {
    return <Pages />;
  }
}

export { AppTest as App };
export default withRouter(AppTest);
