import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import { useAuth0 } from '../react-auth0-wrapper';
import SignInRedirector from './pages/SignInRedirector/SignInRedirector';
import SignOutRedirector from './pages/SignOutRedirector/SignOutRedirector';
import MainApp from './pages/MainApp/MainApp';
import MarketingSite from './pages/MarketingSite/MarketingSite';
import { ThunkDispatch } from 'redux-thunk';
import State from '../state';
import { AnyAction } from 'redux';
import { connect } from 'react-redux';
import OrgActions from '../redux/org/OrgActions';
import TenantActions from '../redux/tenant/TenantActions';

interface Props {
  state: State;
  dispatch: ThunkDispatch<State, null, AnyAction>;
}

const AppRouter: React.FC<Props> = (props: Props) => {
  const auth0 = useAuth0();
  if (auth0.loading) return null;

  // props.dispatch(TenantActions.ensureLoaded());
  // console.log(props.state);
  // if (props.state.tenant.loadingStatus !== 'LOADED') {
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
      <Route component={auth0.isAuthenticated ? MainApp : MarketingSite} />
    </Switch>
  </Router>;
};

export default connect(
  (state: State) => ({ state }),
)(AppRouter);
