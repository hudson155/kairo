import React from 'react';
import { Auth0Provider } from '../react-auth0-wrapper';
import { rootUrl } from '../index';
import AppRouter from './AppRouter';

// A function that routes the user to the right place after signing in.
// eslint-disable-next-line @typescript-eslint/no-explicit-any
const onRedirectCallback = (appState: any): any => {
  window.history.replaceState(
    {},
    document.title,
    appState && appState.targetUrl ? appState.targetUrl : window.location.pathname,
  );
};

const App: React.FC = () => {
  return <Auth0Provider
    domain="limber.auth0.com"
    client_id={process.env['REACT_APP_AUTH0_CLIENT_ID']}
    redirect_uri={rootUrl}
    audience="https://limber.auth0.com/api/v2/"
    // eslint-disable-next-line @typescript-eslint/ban-ts-ignore
    // @ts-ignore-line
    onRedirectCallback={onRedirectCallback}
  >
    <AppRouter />
  </Auth0Provider>;
};

export default App;
