import React from 'react';
import { Redirect, Route, Switch } from 'react-router-dom';
import { ThunkDispatch } from 'redux-thunk';
import { AnyAction } from 'redux';
import { connect } from 'react-redux';
import { useAuth0 } from '../../../react-auth0-wrapper';
import State from '../../../state';
import Loading from '../../components/Loading/Loading';
import AuthActions from '../../../redux/auth/AuthActions';
import EventsPage from './pages/EventsPage/EventsPage';
import Page from '../../components/Page/Page';
import AppPageHeader from './components/AppPageHeader/AppPageHeader';
import AppPageFooter from './components/AppPageFooter/AppPageFooter';

interface Props {
  state: State;
  dispatch: ThunkDispatch<{}, {}, AnyAction>;
}

const MainApp: React.FC<Props> = (props: Props) => {
  const auth0 = useAuth0();
  if (auth0.loading) return null;

  if (props.state.orgs.loadingStatus === 'NOT_LOADED_OR_LOADING') { // This should be auth.loadingStatus
    auth0.getTokenSilently().then((jwt: string) => props.dispatch(AuthActions.setJwt(jwt)));
    return <Loading>Loading your organizations.</Loading>;
  }

  return (
    <Page header={<AppPageHeader />} footer={<AppPageFooter />}>
      <Switch>
        <Route key="/" path="/" exact>
          <Redirect to="/events" />
        </Route>,
        <Route key="/events" path="/events" exact component={EventsPage} />,
      </Switch>
    </Page>
  );
};

export default connect(
  (state: State) => ({ state }),
)(MainApp);
