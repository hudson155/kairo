import { organizationApiState } from 'api/OrganizationApi';
import { rootHost } from 'metadata';
import OrganizationRep from 'rep/OrganizationRep';
import spring from 'state/util/spring';

const organizationState = spring<OrganizationRep>({
  key: 'core/organization',
  get: async ({ get }) => {
    const organizationApi = get(organizationApiState);
    const organization = await organizationApi.getByHostname(rootHost);
    if (!organization) throw new Error('No organization found.');
    return organization;
  },
});

export default organizationState;
