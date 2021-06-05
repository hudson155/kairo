import { useAuth0 } from '@auth0/auth0-react';
import React from 'react';
import { Route, Switch } from 'react-router-dom';
import AuthenticatedRouter from './AuthenticatedRouter';
import SignInPage, { signInPagePath } from './pages/SignInPage';
import SignOutPage, { signOutPagePath } from './pages/SignOutPage';
import UnauthenticatedRouter from './UnauthenticatedRouter';

/**
 * The sign in and sign out routes are always accessible, mostly for debugging and local
 * development. The rest of the routes are defined by either the authenticated or unauthenticated
 * router.
 */
const RootRouter: React.FC = () => {
  const auth = useAuth0();

  return (
    <Switch>
      <Route component={SignInPage} exact={true} path={signInPagePath()} />
      <Route component={SignOutPage} exact={true} path={signOutPagePath()} />
      <Route>{auth.isAuthenticated ? <AuthenticatedRouter /> : <UnauthenticatedRouter />}</Route>
    </Switch>
  );
};

export default RootRouter;
