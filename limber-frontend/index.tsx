import * as React from 'react';
import { render } from 'react-dom';

const App: React.SFC = () => <h1>Hello, World!</h1>;

render(<App />, document.getElementById('root'));

if (module.hot) {
  module.hot.accept();
}
