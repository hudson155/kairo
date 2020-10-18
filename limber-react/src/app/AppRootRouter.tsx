import { useAuth0 } from '@auth0/auth0-react';
import React, { ReactElement, ReactNode } from 'react';
import { Route, Switch } from 'react-router-dom';

import { app } from '../app';
import GlobalStateProvider from '../provider/GlobalStateProvider';
import { TenantRepComplete } from '../rep/Tenant';

import AppFeatureRouter from './AppFeatureRouter';
import AppUnauthenticatedRouter from './AppUnauthenticatedRouter';
import MainAppNavbar from './components/MainAppNavbar';
import LoadingPage from './pages/LoadingPage/LoadingPage';
import SignInPage from './pages/SignInPage/SignInPage';
import SignOutPage from './pages/SignOutPage/SignOutPage';

interface Props {
  readonly tenant: TenantRepComplete;
}

function AppRootRouter(props: Props): ReactElement {
  const auth = useAuth0();

  if (auth.isLoading) return <LoadingPage message="Identifying you..." />;

  const subRouter: ReactNode = auth.isAuthenticated ? (
    <GlobalStateProvider orgGuid={props.tenant.orgGuid}>
      <Route component={AppFeatureRouter} exact={false} path={app.rootPath} />
    </GlobalStateProvider>
  ) : <Route exact={false} path={app.rootPath}><AppUnauthenticatedRouter name={props.tenant.name} /></Route>;

  return (
    <>
      <MainAppNavbar />
      <Switch>
        <Route component={SignInPage} exact={true} path="/signin" />
        <Route component={SignOutPage} exact={true} path="/signout" />
        {subRouter}
      </Switch>
    </>
  );
}

export default AppRootRouter;
