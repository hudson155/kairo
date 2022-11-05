import React from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import FeatureRep from 'rep/FeatureRep';
import featuresState from 'state/core/features';
import Feature from './Feature';

const FeatureRouter: React.FC = () => {
  const features = useRecoilValue(featuresState);
  const defaultFeature = findDefaultFeature(features);

  return (
    <Routes>
      <Route
        element={<Navigate to={defaultFeature.rootPath} />}
        path="/"
      />
      {
        features.map((feature) => (
          <Route
            key={feature.guid}
            element={<Feature feature={feature} />}
            path={`${feature.rootPath}/*`}
          />
        ))
      }
    </Routes>
  );
};

export default FeatureRouter;

const findDefaultFeature = (features: FeatureRep[]): FeatureRep => {
  const defaultFeature = features.find((feature) => feature.isDefault);
  if (!defaultFeature) throw new Error('No default feature found.');
  return defaultFeature;
};
