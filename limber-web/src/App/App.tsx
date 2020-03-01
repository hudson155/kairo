import React from 'react';
import { connect } from 'react-redux';
import { AnyAction } from 'redux';
import { ThunkDispatch } from 'redux-thunk';
import { rootUrl } from '../index';
import TenantModel from '../models/tenant/TenantModel';
import { Auth0Provider } from '../react-auth0-wrapper';
import Actions from '../redux/Actions';
import LoadableState from '../redux/util/LoadableState';
import State from '../state';
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

interface Props {
  tenantState: LoadableState<TenantModel>;
  dispatch: ThunkDispatch<State, null, AnyAction>;
}

const App: React.FC<Props> = (props: Props) => {
  props.dispatch(Actions.tenant.ensureLoaded());
  console.log(props.tenantState);
  if (props.tenantState.loadingStatus !== 'LOADED') {
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
    client_id={props.tenantState.model!.auth0ClientId}
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
  (state: State) => ({ tenantState: state.tenant }),
)(App);
