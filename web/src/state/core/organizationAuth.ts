import { organizationAuthApiState } from 'api/OrganizationAuthApi';
import { getRootHost } from 'metadata';
import OrganizationAuthRep from 'rep/OrganizationAuthRep';
import spring from 'state/util/spring';

const organizationAuthState = spring<OrganizationAuthRep>({
  key: 'core/organizationAuth',
  get: async ({ get }) => {
    const authApi = get(organizationAuthApiState);
    const auth = await authApi.getByHostname(getRootHost());
    if (!auth) throw new Error('No auth found.');
    return auth;
  },
});

export default organizationAuthState;
