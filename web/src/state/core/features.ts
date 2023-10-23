import { featureApiState } from 'api/FeatureApi';
import { atom, selector } from 'recoil';
import FeatureRep from 'rep/FeatureRep';
import organizationIdState from 'state/core/organizationId';

const featuresState = atom<FeatureRep[]>({
  key: 'core/features',
  default: selector({
    key: 'core/features-default',
    get: async ({ get }) => {
      const featureApi = get(featureApiState);
      const organizationId = get(organizationIdState);
      const features = await featureApi.listByOrganization(organizationId);
      return sortFeatures(features);
    },
  }),
});

export default featuresState;

/**
 * Features are sorted in a stable order, with the default feature first.
 */
const sortFeatures = (features: FeatureRep[]): FeatureRep[] => {
  if (features.length === 0) throw new Error('No features found.');
  const defaultFeature = features.find((feature) => feature.isDefault);
  if (!defaultFeature) throw new Error('No default feature found.');
  return [defaultFeature, ...features.filter((feature) => feature.id !== defaultFeature.id)];
};
