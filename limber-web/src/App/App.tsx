import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import { useAuth0 } from '../react-auth0-wrapper';
import SignInPage from './pages/SignInPage/SignInPage';
import SignOutPage from './pages/SignOutPage/SignOutPage';
import MainApp from './pages/MainApp/MainApp';
import MarketingSite from './pages/MarketingSite/MarketingSite';

const App: React.FC = () => {
  const auth0 = useAuth0();
  if (auth0.loading) return null;

  return (
    <Router>
      <Switch>
        <Route key="/signin" path="/signin" exact component={SignInPage} />
        <Route key="/signout" path="/signout" exact component={SignOutPage} />
        <Route key="" component={auth0.isAuthenticated ? MainApp : MarketingSite} />
      </Switch>
    </Router>
  );
};

export default App;
