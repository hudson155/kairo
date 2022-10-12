import { atom } from 'recoil';
import FeatureRep from 'rep/FeatureRep';

const featureState = atom<FeatureRep>({
  key: 'core/feature',
});

export default featureState;
