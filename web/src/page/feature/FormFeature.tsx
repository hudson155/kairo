import HeaderSection from 'component/section/HeaderSection';
import React from 'react';
import FeatureRep from 'rep/FeatureRep';

interface Props {
  feature: FeatureRep;
}

const FormFeature: React.FC<Props> = ({ feature }) => {
  return (
    <HeaderSection title={feature.name} />
  );
};

export default FormFeature;
