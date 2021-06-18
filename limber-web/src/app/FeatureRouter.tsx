import React from 'react';
import { Redirect, Route, Switch } from 'react-router-dom';
import { useOrg } from '../provider/AuthenticatedStateProvider/OrgProvider';
import FeatureRep from '../rep/FeatureRep';
import ErrorPage from './pages/ErrorPage';
import OrgSettingsPage, { orgSettingsPagePath } from './pages/OrgSettingsPage';

/**
 * Dynamic router powered by the organization's features.
 */
const FeatureRouter: React.FC = () => {
  const { features } = useOrg();
  const defaultFeature = findDefaultFeature(features);

  if (features.length === 0) {
    return <ErrorPage errorMessage="No features are configured." />;
  }

  // TODO: Implement routing dependent on feature type, and remove the boilerplate stuff.
  return (
    <>
      <h1>Limber</h1>
      <p>This is Limber.</p>
      <Switch>
        <Route exact={true} path={orgSettingsPagePath()} component={OrgSettingsPage} />
        {features.map(feature => (
          <Route key={feature.guid} exact={true} path={feature.path}>{feature.name}</Route>
        ))}
        {defaultFeature && (
          <Route exact={true} path="/"><Redirect to={defaultFeature.path} /></Route>
        )}
        {/* TODO: Catch-all 404 */}
      </Switch>
    </>
  );
};

export default FeatureRouter;

function findDefaultFeature(features: FeatureRep.Complete[]): FeatureRep.Complete | undefined {
  const defaultFeatures = features.filter(feature => feature.isDefaultFeature);
  if (defaultFeatures.length === 0) {
    return undefined;
  }
  if (defaultFeatures.length !== 1) {
    console.error('There are multiple default features.'); // Fail gracefully.
  }
  return defaultFeatures[0];
}
