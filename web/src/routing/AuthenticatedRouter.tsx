import BaseLayout from 'layout/BaseLayout/BaseLayout';
import { adminSettingsRoute } from 'page/adminSettings/AdminSettingsPageRoute';
import FeaturePage from 'page/feature/FeaturePage';
import LoadingPage from 'page/loading/LoadingPage';
import { organizationSettingsRoute } from 'page/organizationSettings/OrganizationSettingsPageRoute';
import React, { Suspense } from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import defaultFeatureState from 'state/core/defaultFeature';
import featuresState from 'state/core/features';

const AuthenticatedRouter: React.FC = () => {
  const features = useRecoilValue(featuresState);
  const defaultFeature = useRecoilValue(defaultFeatureState);

  const defaultFeatureRedirect = (
    <Route
      element={<Navigate replace={true} to={defaultFeature.rootPath} />}
      path="/"
    />
  );

  const featureRoutes = features.map((feature) => (
    <Route
      key={feature.guid}
      element={<FeaturePage feature={feature} />}
      path={`${feature.rootPath}/*`}
    />
  ));

  return (
    <BaseLayout>
      <Suspense fallback={<LoadingPage />}>
        <Routes>
          {defaultFeatureRedirect}
          {organizationSettingsRoute()}
          {adminSettingsRoute()}
          {featureRoutes}
        </Routes>
      </Suspense>
    </BaseLayout>
  );
};

export default AuthenticatedRouter;
