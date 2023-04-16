import Page from 'component/page/Page';
import HeaderSection from 'component/section/HeaderSection';
import React from 'react';
import FeatureRep from 'rep/FeatureRep';

interface Props {
  feature: FeatureRep;
}

const FormFeature: React.FC<Props> = ({ feature }) => {
  return (
    <Page>
      <HeaderSection title={feature.name} />
    </Page>
  );
};

export default FormFeature;
