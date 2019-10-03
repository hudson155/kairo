import { FunctionalComponent, h } from 'preact';
import { Router } from 'preact-router';
import HomePage from './pages/HomePage/HomePage';
import NotFoundPage from './pages/NotFoundPage/NotFoundPage';

const App: FunctionalComponent = () => {
  return <Router>
    <HomePage path="/" />
    <NotFoundPage default />
  </Router>;
};

export default App;
