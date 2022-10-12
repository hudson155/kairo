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
    if (features.length === 0) throw new Error('No Features found.');
    return features;
  },
});

export default featuresState;
