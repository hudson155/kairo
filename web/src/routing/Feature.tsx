import React, { useCallback } from 'react';
import { MutableSnapshot, RecoilRoot } from 'recoil';
import FeatureRep from 'rep/FeatureRep';
import featureState from 'state/core/feature';

interface Props {
  feature: FeatureRep;
}

const Feature: React.FC<Props> = ({ feature }) => {
  const initializeState = useCallback((snapshot: MutableSnapshot) => {
    snapshot.set(featureState, feature);
  }, [feature]);

  return (
    <RecoilRoot initializeState={initializeState}>
      {null}
    </RecoilRoot>
  );
};

export default Feature;
