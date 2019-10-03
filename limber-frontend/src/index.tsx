import { h, render } from 'preact';
import App from './App/App';

render(<App />, document.getElementById('root') as HTMLElement);

if (module.hot) {
  module.hot.accept();
}
