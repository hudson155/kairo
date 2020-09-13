import React, { ReactNode } from 'react';
import { useAuth0 } from '@auth0/auth0-react';
import { Route, Switch } from 'react-router-dom';
import { app } from '../app';
import AppFeatureRouter from './AppFeatureRouter';
import GlobalStateProvider from '../provider/GlobalStateProvider';
import AppUnauthenticatedRouter from './AppUnauthenticatedRouter';
import SignOutPage from './components/SignOutPage/SignOutPage';
import SignInPage from './components/SignInPage/SignInPage';
import LoadingPage from './components/LoadingPage/LoadingPage';

interface Props {
  readonly orgGuid: string;
}

const AppRootRouter: React.FC<Props> = (props) => {
  const auth = useAuth0();

  if (auth.isLoading) return <LoadingPage message="Identifying you..." />;

  const subRouter: ReactNode = auth.isAuthenticated ? (
    <GlobalStateProvider orgGuid={props.orgGuid}>
      <Route path={app.rootPath} exact={false} component={AppFeatureRouter} />
    </GlobalStateProvider>
  ) : <Route path={app.rootPath} exact={false} component={AppUnauthenticatedRouter} />;

  return (
    <Switch>
      <Route path="/signin" exact={true} component={SignInPage} />
      <Route path="/signout" exact={true} component={SignOutPage} />
      {subRouter}
    </Switch>
  );
};

export default AppRootRouter;
