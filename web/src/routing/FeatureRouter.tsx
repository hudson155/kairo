import BaseLayout from 'layout/BaseLayout/BaseLayout';
import LoadingPage from 'page/loadingPage/LoadingPage';
import React, { Suspense } from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import Feature from 'routing/Feature';
import defaultFeatureState from 'state/core/defaultFeature';
import featuresState from 'state/core/features';

const FeatureRouter: React.FC = () => {
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
      element={<Feature feature={feature} />}
      path={`${feature.rootPath}/*`}
    />
  ));

  return (
    <BaseLayout>
      <Suspense fallback={<LoadingPage />}>
        <Routes>
          {defaultFeatureRedirect}
          {featureRoutes}
        </Routes>
      </Suspense>
    </BaseLayout>
  );
};

export default FeatureRouter;
