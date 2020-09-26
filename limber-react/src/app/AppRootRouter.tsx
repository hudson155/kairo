import { useAuth0 } from '@auth0/auth0-react';
import React, { ReactElement, ReactNode } from 'react';
import { Route, Switch } from 'react-router-dom';

import { app } from '../app';
import GlobalStateProvider from '../provider/GlobalStateProvider';

import AppFeatureRouter from './AppFeatureRouter';
import AppUnauthenticatedRouter from './AppUnauthenticatedRouter';
import LoadingPage from './pages/LoadingPage/LoadingPage';
import SignInPage from './pages/SignInPage/SignInPage';
import SignOutPage from './pages/SignOutPage/SignOutPage';


interface Props {
  readonly orgGuid: string;
}

function AppRootRouter(props: Props): ReactElement {
  const auth = useAuth0();

  if (auth.isLoading) return <LoadingPage message="Identifying you..." />;

  const subRouter: ReactNode = auth.isAuthenticated ? (
    <GlobalStateProvider orgGuid={props.orgGuid}>
      <Route component={AppFeatureRouter} exact={false} path={app.rootPath} />
    </GlobalStateProvider>
  ) : <Route component={AppUnauthenticatedRouter} exact={false} path={app.rootPath} />;

  return (
    <Switch>
      <Route component={SignInPage} exact={true} path="/signin" />
      <Route component={SignOutPage} exact={true} path="/signout" />
      {subRouter}
    </Switch>
  );
}

export default AppRootRouter;
