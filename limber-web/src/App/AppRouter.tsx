import React from 'react';
import { connect } from 'react-redux';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import { AnyAction } from 'redux';
import { ThunkDispatch } from 'redux-thunk';
import { TD } from '../index';
import TenantModel from '../models/tenant/TenantModel';
import LoadableState from '../redux/util/LoadableState';
import State from '../state';
import MainApp from './pages/MainApp/MainApp';
import MarketingSite from './pages/MarketingSite/MarketingSite';
import SignInRedirector from './pages/SignInRedirector/SignInRedirector';
import SignOutRedirector from './pages/SignOutRedirector/SignOutRedirector';
import { useAuth } from './useAuth';

interface Props {
  tenantState: LoadableState<TenantModel>;
  dispatch: TD;
}

const AppRouter: React.FC<Props> = (props: Props) => {
  const auth = useAuth();
  if (auth.isLoading()) return null;

  // props.dispatch(TenantActions.ensureLoaded());
  // console.log(props.tenantState);
  // if (props.tenantState.loadingStatus !== 'LOADED') {
  //   /**
  //    * Don't render anything if the tenant loading status is not loaded yet. Normally we wouldn't
  //    * care to add a restriction like this because we'd rather do a partial load, but for the case
  //    * of tenant at the app level, it's important to wait to load before continuing because the
  //    * Auth0 SPA wrapper requires tenant information.
  //    */
  //   return null;
  // }

  return <Router>
    <Switch>
      <Route path="/signin" exact component={SignInRedirector} />
      <Route path="/signout" exact component={SignOutRedirector} />
      <Route component={auth.isAuthenticated() ? MainApp : MarketingSite} />
    </Switch>
  </Router>;
};

export default connect(
  (state: State) => ({ tenantState: state.tenant }),
)(AppRouter);
