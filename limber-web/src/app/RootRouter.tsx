import React from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import SignInPage, { signInPagePath } from './pages/SignInPage/SignInPage';
import SignOutPage, { signOutPagePath } from './pages/SignOutPage/SignOutPage';

/**
 * TODO: Build out the root router properly.
 */
const RootRouter: React.FC = () => {
  return (
    <BrowserRouter>
      <Switch>
        <Route component={SignInPage} exact={true} path={signInPagePath()} />
        <Route component={SignOutPage} exact={true} path={signOutPagePath()} />
        <Route>
          <h1>Limber</h1>
          <p>This is Limber.</p>
        </Route>
      </Switch>
    </BrowserRouter>
  );
};

export default RootRouter;
