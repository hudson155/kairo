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

const store = createStore(rootReducer, applyMiddleware(thunk));

// A function that routes the user to the right place after signing in.
const onRedirectCallback = (appState: any) => {
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
      redirect_uri={window.location.origin}
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
