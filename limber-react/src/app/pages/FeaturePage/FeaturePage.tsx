import React, { ReactNode } from 'react';
import FeatureProvider from '../../../provider/FeatureProvider';
import { FeatureRepComplete } from '../../../rep/Feature';
import HomePage from '../HomePage/HomePage';
import NotFoundPage from '../NotFoundPage/NotFoundPage';
import FormsFeaturePage from '../FormsFeaturePage/FormsFeaturePage';

interface Props {
  readonly feature: FeatureRepComplete;
}

const FeaturePage: React.FC<Props> = (props) => {
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
};

export default FeaturePage;
