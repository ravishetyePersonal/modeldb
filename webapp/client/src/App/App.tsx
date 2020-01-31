import React from 'react';
import { RouteComponentProps, withRouter } from 'react-router';

import Pages from 'pages';

type AllProps = RouteComponentProps;

class App extends React.PureComponent<AllProps> {
  public render() {
    return <Pages />;
  }
}

export { App };
export default withRouter(App);
