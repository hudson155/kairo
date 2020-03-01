import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import { AnyAction, applyMiddleware, createStore } from 'redux';
import thunk, { ThunkAction } from 'redux-thunk';
import App from './App/App';
import './index.css';
import rootReducer from './redux/reducers';
import * as serviceWorker from './serviceWorker';
import State from './state';

export type TA = ThunkAction<Promise<void>, State, null, AnyAction>;

export const rootDomain = window.location.host;
export const rootUrl = `${window.location.protocol}//${rootDomain}`;
export const store = createStore(rootReducer, applyMiddleware(thunk));

ReactDOM.render(
  <Provider store={store}>
    <App />
  </Provider>,
  document.getElementById('root'),
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
