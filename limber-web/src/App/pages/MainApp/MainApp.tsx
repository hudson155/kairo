import React from 'react';
import { connect } from 'react-redux';
import { Redirect, Route, Switch } from 'react-router-dom';
import { TD } from '../../../index';
import FeatureModel from '../../../models/org/FeatureModel';
import Actions from '../../../redux/Actions';
import AuthActions from '../../../redux/auth/AuthActions';
import { assertLoaded } from '../../../redux/util/LoadableState';
import State from '../../../state';
import Footer from '../../components/Footer/Footer';
import Page from '../../components/Page/Page';
import { useAuth } from '../../useAuth';
import MainAppNavbar from './components/MainAppNavbar/MainAppNavbar';
import FormInstancesListPage from './pages/FormInstancesListPage/FormInstancesListPage';

function determineDefaultFeature(features: FeatureModel[]): FeatureModel {
  const featureMarkedAsDefault = features.find(feature => feature.isDefaultFeature);
  if (featureMarkedAsDefault) return featureMarkedAsDefault;
  return features[0];
}

interface Props {
  state: State;
  dispatch: TD;
}

const MainApp: React.FC<Props> = (props: Props) => {
  const auth = useAuth();

  props.dispatch(AuthActions.ensureJwtIsSet(auth.getTokenSilently)).then(() => {
    props.dispatch(Actions.org.ensureLoaded());
    props.dispatch(Actions.user.ensureLoaded());
  });
  if (props.state.auth.loadingStatus !== 'LOADED' || props.state.org.loadingStatus !== 'LOADED' || props.state.user.loadingStatus !== 'LOADED') {
    /**
     * Don't render anything if the auth, org, or user loading status is not loaded yet. Normally we wouldn't care to
     * add a restriction like this because we'd rather do a partial load, but for the case of auth at the app level,
     * it's important to wait to load before continuing in order to prevent actions that cause API requests from being
     * fired without a JWT, and for the case of org or user at the app level, it's not useful to render anything without
     * the most basic of information.
     */
    return null;
  }

  const features = assertLoaded(props.state.org).features;
  const defaultFeature = determineDefaultFeature(features);

  return <Page header={<MainAppNavbar />} footer={<Footer />}>
    <Switch>
      <Route path="/" exact>
        <Redirect to={defaultFeature.path} />
      </Route>,
      {features!.map(feature => {
        return <Route key={feature.path} path={feature.path} exact component={FormInstancesListPage} />;
      })}
    </Switch>
  </Page>;
};

export default connect((state: State) => ({
  state,
}))(MainApp);
