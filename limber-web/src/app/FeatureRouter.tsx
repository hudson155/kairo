import React from 'react';
import { Redirect, Route, Switch } from 'react-router-dom';
import { useOrg } from '../provider/AuthenticatedStateProvider/OrgProvider';
import FeatureRep from '../rep/FeatureRep';
import ErrorPage from './pages/ErrorPage';

const FeatureRouter: React.FC = () => {
  const { features } = useOrg();
  const defaultFeature = findDefaultFeature(features);

  if (features.length === 0) {
    return <ErrorPage errorMessage="No features are configured." />;
  }

  return (
    <>
      <h1>Limber</h1>
      <p>This is Limber.</p>
      <Switch>
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
  if (defaultFeatures.length === 0) return undefined;
  if (defaultFeatures.length === 1) return defaultFeatures[0];
  throw new Error('There are multiple default features.');
}
