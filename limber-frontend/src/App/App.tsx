import { FunctionalComponent, h } from 'preact';
import { Router } from 'preact-router';
import EventsPage from './pages/EventsPage/EventsPage';
import NotFoundPage from './pages/NotFoundPage/NotFoundPage';

const App: FunctionalComponent = () => {
  return <Router>
    <EventsPage path="/events" />
    <NotFoundPage default />
  </Router>;
};

export default App;
