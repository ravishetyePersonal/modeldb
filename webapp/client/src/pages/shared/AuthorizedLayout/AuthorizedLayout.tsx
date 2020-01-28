import * as React from 'react';

import Layout from 'core/shared/view/pages/Layout/Layout';

type ILocalProps = Omit<React.ComponentProps<typeof Layout>, 'userBar'>;

const AuthorizedLayout = Layout;

export type IAuthorizedLayoutLocalProps = ILocalProps;
export default AuthorizedLayout;
