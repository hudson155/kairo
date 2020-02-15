import React from 'react';
import { Redirect, Route, Switch } from 'react-router-dom';
import { AnyAction } from 'redux';
import { connect } from 'react-redux';
import State from '../../../state';
import AuthActions from '../../../redux/auth/AuthActions';
import EventsPage from './pages/EventsPage/EventsPage';
import Page from '../../components/Page/Page';
import MainAppNavbar from './components/MainAppNavbar/MainAppNavbar';
import Footer from '../../components/Footer/Footer';
import { ThunkDispatch } from 'redux-thunk';
import { LoadingStatus } from '../../../redux/util/LoadingStatus';
import UserActions from '../../../redux/user/UserActions';
import OrgActions from '../../../redux/org/OrgActions';
import { useAuth } from '../../useAuth';

interface Props {
  authLoadingStatus: LoadingStatus;
  dispatch: ThunkDispatch<State, null, AnyAction>;
}

const MainApp: React.FC<Props> = (props: Props) => {
  const auth = useAuth();

  props.dispatch(AuthActions.ensureSetJwt(auth.getTokenSilently)).then(() => {
    props.dispatch(OrgActions.ensureLoaded());
    props.dispatch(UserActions.ensureLoaded());
  });
  if (props.authLoadingStatus !== 'LOADED') {
    /**
     * Don't render anything if the auth loading status is not loaded yet. Normally we wouldn't care
     * to add a restriction like this because we'd rather do a partial load, but for the case of
     * auth at the app level, it's important to wait to load before continuing in order to prevent
     * actions that cause API requests from being fired without a JWT.
     */
    return null;
  }

  return <Page header={<MainAppNavbar />} footer={<Footer />}>
    <Switch>
      <Route path="/" exact>
        <Redirect to="/events" />
      </Route>,
      <Route path="/events" exact component={EventsPage} />,
    </Switch>
  </Page>;
};

export default connect(
  (state: State) => ({ authLoadingStatus: state.auth.loadingStatus }),
)(MainApp);
