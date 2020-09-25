import React, { ReactElement } from 'react';
import { Redirect, Route, Switch } from 'react-router-dom';

import { app } from '../app';
import { useOrg } from '../provider/OrgProvider';
import { getDefaultFeature } from '../rep/Feature';

import FeaturePage from './pages/FeaturePage/FeaturePage';
import NotFoundPage from './pages/NotFoundPage/NotFoundPage';
import OrgSettingsPage from './pages/OrgSettingsPage/OrgSettingsPage';


function AppFeatureRouter(): ReactElement {
  const org = useOrg();

  const features = org.features;
  const defaultFeature = getDefaultFeature(features);

  return (
    <Switch>
      {defaultFeature && <Route exact={true} path={app.rootPath}><Redirect to={defaultFeature.path} /></Route>}
      <Route component={OrgSettingsPage} exact={false} path="/settings/org" />
      {features.map(feature => (
        <Route exact={false} key={feature.guid} path={feature.path}>
          <FeaturePage feature={feature} />
        </Route>
      ),
      )}
      <Route component={NotFoundPage} exact={false} path={app.rootPath} />
    </Switch>
  );
}

export default AppFeatureRouter;
