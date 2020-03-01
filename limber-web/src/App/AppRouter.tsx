import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import MainApp from './pages/MainApp/MainApp';
import MarketingSite from './pages/MarketingSite/MarketingSite';
import SignInRedirector from './pages/SignInRedirector/SignInRedirector';
import SignOutRedirector from './pages/SignOutRedirector/SignOutRedirector';
import { useAuth } from './useAuth';

const AppRouter: React.FC = () => {
  const auth = useAuth();
  if (auth.isLoading()) return null;

  return <Router>
    <Switch>
      <Route path="/signin" exact component={SignInRedirector} />
      <Route path="/signout" exact component={SignOutRedirector} />
      <Route component={auth.isAuthenticated() ? MainApp : MarketingSite} />
    </Switch>
  </Router>;
};

export default AppRouter;
