import React, { ReactElement, ReactNode } from 'react';

import FeatureProvider from '../../../provider/FeatureProvider';
import { FeatureRepComplete } from '../../../rep/Feature';
import FormsFeaturePage from '../FormsFeaturePage/FormsFeaturePage';
import HomePage from '../HomePage/HomePage';
import NotFoundPage from '../NotFoundPage/NotFoundPage';

interface Props {
  readonly feature: FeatureRepComplete;
}

function FeaturePage(props: Props): ReactElement {
  let featureComponent: ReactNode;
  switch (props.feature.type) {
  case 'FORMS':
    featureComponent = <FormsFeaturePage />;
    break;
  case 'HOME':
    featureComponent = <HomePage />;
    break;
  default:
    featureComponent = <NotFoundPage />;
  }

  return <FeatureProvider value={props.feature}>{featureComponent}</FeatureProvider>;
}

export default FeaturePage;
