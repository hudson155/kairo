import { organizationApiState } from 'api/OrganizationApi';
import OrganizationRep from 'rep/OrganizationRep';
import organizationGuidState from 'state/core/organizationGuid';
import spring from 'state/util/spring';

const organizationState = spring<OrganizationRep>({
  key: 'core/organization',
  get: async ({ get }) => {
    const organizationApi = get(organizationApiState);
    const organizationGuid = get(organizationGuidState);
    const organization = await organizationApi.get(organizationGuid);
    if (!organization) throw new Error('No organization found.');
    return organization;
  },
});

export default organizationState;
