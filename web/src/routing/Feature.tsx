import React from 'react';
import FeatureRep from 'rep/FeatureRep';

const FormFeature = React.lazy(() => import(`routing/FormFeature`));
const PlaceholderFeature = React.lazy(() => import(`routing/PlaceholderFeature`));

interface Props {
  feature: FeatureRep;
}

const Feature: React.FC<Props> = ({ feature }) => {
  switch (feature.type) {
  case `Form`:
    return <FormFeature feature={feature} />;
  case `Placeholder`:
    return <PlaceholderFeature feature={feature} />;
  default:
    throw new Error(`Unsupported feature type: ${feature.type}.`);
  }
};

export default Feature;
