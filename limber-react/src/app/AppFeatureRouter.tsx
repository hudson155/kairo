import React from 'react';
import { useOrg } from '../provider/OrgProvider';
import { Redirect, Route, Switch } from 'react-router-dom';
import { getDefaultFeature } from '../rep/Feature';
import { app } from '../app';
import OrgSettingsPage from './pages/OrgSettingsPage/OrgSettingsPage';
import FeaturePage from './pages/FeaturePage/FeaturePage';
import NotFoundPage from './pages/NotFoundPage/NotFoundPage';

const AppFeatureRouter: React.FC = () => {
  const org = useOrg();

  const features = org.features;
  const defaultFeature = getDefaultFeature(features);

  return (
    <Switch>
      {defaultFeature && <Route path={app.rootPath} exact={true}><Redirect to={defaultFeature.path} /></Route>}
      <Route path="/settings/org" exact={false} component={OrgSettingsPage} />
      {features.map(feature => (
          <Route key={feature.guid} path={feature.path} exact={false}>
            <FeaturePage feature={feature} />
          </Route>
        ),
      )}
      <Route path={app.rootPath} exact={false} component={NotFoundPage} />
    </Switch>
  );
};

export default AppFeatureRouter;
