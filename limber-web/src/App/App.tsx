import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import { useAuth0 } from '../react-auth0-wrapper';
import SignInRedirector from './pages/SignInRedirector/SignInRedirector';
import SignOutRedirector from './pages/SignOutRedirector/SignOutRedirector';
import MainApp from './pages/MainApp/MainApp';
import MarketingSite from './pages/MarketingSite/MarketingSite';

const App: React.FC = () => {
  const auth0 = useAuth0();
  if (auth0.loading) return null;

  return <Router>
    <Switch>
      <Route path="/signin" exact component={SignInRedirector} />
      <Route path="/signout" exact component={SignOutRedirector} />
      <Route component={auth0.isAuthenticated ? MainApp : MarketingSite} />
    </Switch>
  </Router>;
};

export default App;
