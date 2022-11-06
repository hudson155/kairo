import { selector } from 'recoil';
import FeatureRep from 'rep/FeatureRep';
import featuresState from 'state/core/features';

const defaultFeatureState = selector<FeatureRep>({
  key: `core/defaultFeature`,
  get: ({ get }) => {
    const features = get(featuresState);
    return features[0]; // The default feature is always first. See [featuresState].
  },
});

export default defaultFeatureState;
