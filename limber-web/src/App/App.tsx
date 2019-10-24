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

  const authenticatedRoutes: ReactNodeArray = [
    <Route key="/" path="/" exact>
      <Redirect to="/events" />
    </Route>,
    <Route key="/events" path="/events" exact component={EventsPage} />,
  ];

  const unauthenticatedRoutes: ReactNodeArray = [<Route key="/" path="/" exact component={MarketingSiteHomePage} />];

  const regardlessRoutes = [
    <Route key="/signin" path="/signin" exact component={SignInPage} />,
    <Route key="/signout" path="/signout" exact component={SignOutPage} />,
    <Route key="" component={NotFoundPage} />,
  ];

  const routes: ReactNodeArray = [];
  if (isAuthenticated) {
    routes.push(...authenticatedRoutes);
  } else {
    routes.push(...unauthenticatedRoutes);
  }
  routes.push(...regardlessRoutes);

  return (
    <Router>
      <Switch>{routes}</Switch>
    </Router>
  );
};

export default App;
