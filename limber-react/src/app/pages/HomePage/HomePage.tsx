import React from 'react';
import { useFeature } from '../../../provider/FeatureProvider';

const HomePage: React.FC = () => {
  const feature = useFeature();
  return (
    <>
      <h1>{feature.name}</h1>
      <p>This is the homepage.</p>
    </>
  );
};

export default HomePage;
