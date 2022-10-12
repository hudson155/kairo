import { organizationApiState } from 'api/OrganizationApi';
import { rootUrl } from 'metadata';
import OrganizationRep from 'rep/OrganizationRep';
import spring from 'state/util/spring';

const organizationState = spring<OrganizationRep>({
  key: 'core/organization',
  get: async ({ get }) => {
    const organizationApi = get(organizationApiState);
    const organization = await organizationApi.getByHostname(rootUrl);
    if (!organization) throw new Error('No organization found.');
    return organization;
  },
});

export default organizationState;
