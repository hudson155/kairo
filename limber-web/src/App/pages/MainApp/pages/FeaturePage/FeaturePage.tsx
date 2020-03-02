import React from 'react';
import FeatureModel from '../../../../../models/org/FeatureModel';
import NotFoundPage from '../NotFoundPage/NotFoundPage';
import FormInstancesListPage from './FormInstancesListPage/FormInstancesListPage';
import HomePage from './HomePage/HomePage';

interface Props {
  feature: FeatureModel;
}

const FeaturePage: React.FC<Props> = (props: Props) => {
  switch (props.feature.type) {
    case 'FORMS':
      return <FormInstancesListPage />;
    case 'HOME':
      return <HomePage />;
    default:
      return <NotFoundPage />;
  }
};

export default FeaturePage;
