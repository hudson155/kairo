import React, { ReactNodeArray } from 'react';
import { BrowserRouter as Router, Route, Switch, Redirect } from 'react-router-dom';
import NotFoundPage from './pages/NotFoundPage/NotFoundPage';
import EventsPage from './pages/EventsPage/EventsPage';
import { useAuth0 } from '../react-auth0-wrapper';
import Loading from './components/Loading/Loading';
import MarketingSiteHomePage from './pages/MarketingSiteHomePage/MarketingSiteHomePage';
import SignInPage from './pages/SignInPage/SignInPage';
import SignOutPage from './pages/SignOutPage/SignOutPage';

const App: React.FC = () => {
  const { isAuthenticated, loading: loadingAuth0 } = useAuth0();
  if (loadingAuth0) return <Loading />;

  const routes: ReactNodeArray = [];
  if (isAuthenticated) {
    routes.push(
      <Route path="/" exact>
        {() => <Redirect to="/events" />}
      </Route>,
      <Route path="/events" exact component={EventsPage} />,
    );
  } else {
    routes.push(<Route path="/" exact component={MarketingSiteHomePage} />);
  }
  routes.push(
    <Route path="/signin" exact component={SignInPage} />,
    <Route path="/signout" exact component={SignOutPage} />,
    <Route component={NotFoundPage} />,
  );

  return (
    <Router>
      <Switch>{routes}</Switch>
    </Router>
  );
};

export default App;
