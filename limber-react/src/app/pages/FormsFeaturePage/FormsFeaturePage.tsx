import React from 'react';
import { useFeature } from '../../../provider/FeatureProvider';

const FormsFeaturePage: React.FC = () => {
  const feature = useFeature();
  return (
    <>
      <h1>{feature.name}</h1>
      <p>The forms pages have not been written yet.</p>
    </>
  );
};

export default FormsFeaturePage;
