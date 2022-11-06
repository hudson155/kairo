import { organizationAuthApiState } from 'api/OrganizationAuthApi';
import { rootHost } from 'metadata';
import OrganizationAuthRep from 'rep/OrganizationAuthRep';
import spring from 'state/util/spring';

const organizationAuthState = spring<OrganizationAuthRep>({
  key: `core/organizationAuth`,
  get: async ({ get }) => {
    const authApi = get(organizationAuthApiState);
    const auth = await authApi.getByHostname(rootHost);
    if (!auth) throw new Error(`No auth found.`);
    return auth;
  },
});

export default organizationAuthState;
