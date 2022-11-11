import HeaderSection from 'component/section/HeaderSection';
import BaseLayout from 'layout/BaseLayout/BaseLayout';
import React from 'react';
import FeatureRep from 'rep/FeatureRep';

interface Props {
  feature: FeatureRep;
}

const FormFeature: React.FC<Props> = ({ feature }) => {
  return (
    <BaseLayout>
      <HeaderSection title={feature.name} />
    </BaseLayout>
  );
};

export default FormFeature;
