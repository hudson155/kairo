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

  return (
    <Router>
      <Switch>
        <Route key="/signin" path="/signin" exact component={SignInRedirector} />
        <Route key="/signout" path="/signout" exact component={SignOutRedirector} />
        <Route key="" component={auth0.isAuthenticated ? MainApp : MarketingSite} />
      </Switch>
    </Router>
  );
};

export default App;
