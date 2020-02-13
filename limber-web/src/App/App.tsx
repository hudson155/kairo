import React from 'react';
import { Auth0Provider } from '../react-auth0-wrapper';
import { rootUrl } from '../index';
import AppRouter from './AppRouter';
import State from '../state';
import { ThunkDispatch } from 'redux-thunk';
import { AnyAction } from 'redux';
import { connect } from 'react-redux';
import TenantActions from '../redux/tenant/TenantActions';

// A function that routes the user to the right place after signing in.
// eslint-disable-next-line @typescript-eslint/no-explicit-any
const onRedirectCallback = (appState: any): any => {
  window.history.replaceState(
    {},
    document.title,
    appState && appState.targetUrl ? appState.targetUrl : window.location.pathname,
  );
};

interface Props {
  state: State;
  dispatch: ThunkDispatch<State, null, AnyAction>;
}

const App: React.FC<Props> = (props: Props) => {
  props.dispatch(TenantActions.ensureLoaded());
  console.log(props.state);
  if (props.state.tenant.loadingStatus !== 'LOADED') {
    /**
     * Don't render anything if the tenant loading status is not loaded yet. Normally we wouldn't
     * care to add a restriction like this because we'd rather do a partial load, but for the case
     * of tenant at the app level, it's important to wait to load before continuing because the
     * Auth0 SPA wrapper requires tenant information.
     */
    return null;
  }

  return <Auth0Provider
    domain="limber.auth0.com"
    client_id={props.state.tenant.tenant!!.auth0ClientId}
    redirect_uri={rootUrl}
    audience="https://limber.auth0.com/api/v2/"
    // eslint-disable-next-line @typescript-eslint/ban-ts-ignore
    // @ts-ignore-line
    onRedirectCallback={onRedirectCallback}
  >
    <AppRouter />
  </Auth0Provider>;
};

export default connect(
  (state: State) => ({ state }),
)(App);
