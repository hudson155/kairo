import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import { applyMiddleware, createStore } from 'redux';
import App from './App/App';
import './index.css';
import rootReducer from './redux/reducers';
import * as serviceWorker from './serviceWorker';
import thunk from 'redux-thunk';
import { Auth0Provider } from './react-auth0-wrapper';

export const rootUrl = `${window.location.protocol}//${window.location.host}`;
export const store = createStore(rootReducer, applyMiddleware(thunk));

// A function that routes the user to the right place after signing in.
// eslint-disable-next-line @typescript-eslint/no-explicit-any
const onRedirectCallback = (appState: any): any => {
  window.history.replaceState(
    {},
    document.title,
    appState && appState.targetUrl ? appState.targetUrl : window.location.pathname,
  );
};

ReactDOM.render(
  <Provider store={store}>
    <Auth0Provider
      domain="limber.auth0.com"
      client_id={process.env['REACT_APP_AUTH0_CLIENT_ID']}
      redirect_uri={rootUrl}
      audience="https://limber.auth0.com/api/v2/"
      // eslint-disable-next-line @typescript-eslint/ban-ts-ignore
      // @ts-ignore-line
      onRedirectCallback={onRedirectCallback}
    >
      <App />
    </Auth0Provider>
  </Provider>,
  document.getElementById('root'),
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
