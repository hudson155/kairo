import { organizationAuthApiState } from 'api/OrganizationAuthApi';
import OrganizationAuthRep from 'rep/OrganizationAuthRep';
import organizationGuidState from 'state/core/organizationGuid';
import spring from 'state/util/spring';

const organizationAuthState = spring<OrganizationAuthRep>({
  key: 'core/organizationAuth',
  get: async ({ get }) => {
    const authApi = get(organizationAuthApiState);
    const organizationGuid = get(organizationGuidState);
    const auth = await authApi.getByOrganization(organizationGuid);
    if (!auth) throw new Error('No auth found.');
    return auth;
  },
});

export default organizationAuthState;
