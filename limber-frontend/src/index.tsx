import { h, render } from 'preact';
import App from './App/App';
import './index.css';

render(<App />, document.getElementById('root') as HTMLElement);

if (module.hot) {
  module.hot.accept();
}
