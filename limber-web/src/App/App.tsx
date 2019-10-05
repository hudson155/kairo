import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import NotFoundPage from './pages/NotFoundPage/NotFoundPage';
import EventsPage from './pages/EventsPage/EventsPage';

const App: React.FC = () => {
  return (
    <Router>
      <Switch>
        <Route path="/events" exact component={EventsPage} />
        <Route component={NotFoundPage} />
      </Switch>
    </Router>
  );
};

export default App;
