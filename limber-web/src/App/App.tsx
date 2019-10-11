import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import NotFoundPage from './pages/NotFoundPage/NotFoundPage';
import EventsPage from './pages/EventsPage/EventsPage';
import { useAuth0 } from '../react-auth0-wrapper';
import Loading from './components/Loading/Loading';

const App: React.FC = () => {
  const { loading: loadingAuth0, loginWithRedirect: signIn } = useAuth0();
  if (loadingAuth0) return <Loading />;

  return (
    <Router>
      <Switch>
        <Route path="/events" exact component={EventsPage} />
        <Route path="/signin" exact>{() => signIn()}</Route>
        <Route component={NotFoundPage} />
      </Switch>
    </Router>
  );
};

export default App;
