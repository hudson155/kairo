import { featureApiState } from 'api/FeatureApi';
import FeatureRep from 'rep/FeatureRep';
import organizationGuidState from 'state/core/organizationGuid';
import spring from 'state/util/spring';

const featuresState = spring<FeatureRep[]>({
  key: 'core/features',
  get: async ({ get }) => {
    const featureApi = get(featureApiState);
    const organizationGuid = get(organizationGuidState);
    const features = await featureApi.getByOrganization(organizationGuid);
    return sortFeatures(features);
  },
});

export default featuresState;

/**
 * Features are sorted in a stable order, with the default feature first.
 */
const sortFeatures = (features: FeatureRep[]): FeatureRep[] => {
  if (features.length === 0) throw new Error('No Features found.');
  const defaultFeature = features.find((feature) => feature.isDefault);
  if (!defaultFeature) throw new Error('No default feature found.');
  return [defaultFeature, ...features.filter((feature) => feature.guid !== defaultFeature.guid)];
};
