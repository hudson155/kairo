import React, { ReactElement } from 'react';

import { useFeature } from '../../../provider/FeatureProvider';

function HomePage(): ReactElement {
  const feature = useFeature();
  return (
    <>
      <h1>{feature.name}</h1>
      <p>This is the homepage.</p>
    </>
  );
}

export default HomePage;
