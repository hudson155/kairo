import React from 'react';
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
    <Routes>
      {defaultFeatureRedirect}
      {featureRoutes}
    </Routes>
  );
};

export default FeatureRouter;
