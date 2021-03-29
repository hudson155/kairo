import React from 'react';
import { Redirect, Route, Switch } from 'react-router-dom';
import SignedOutPage, { signedOutPagePath } from './pages/SignedOutPage/SignedOutPage';
import { signInPageDescriptor } from './pages/SignInPage/SignInPage';

const UnauthenticatedRouter: React.FC = () => {
  return (
    <Switch>
      <Route component={SignedOutPage} exact={true} path={signedOutPagePath()} />
      <Route><Redirect to={signInPageDescriptor()} /></Route>
    </Switch>
  );
};

export default UnauthenticatedRouter;
