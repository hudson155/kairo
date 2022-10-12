import { selector } from 'recoil';
import organizationAuthState from 'state/core/organizationAuth';

const organizationAuth0IdState = selector<string>({
  key: 'auth/organizationAuth0Id',
  get: async ({ get }) => {
    const auth = get(organizationAuthState);
    return auth.auth0OrganizationId;
  },
});

export default organizationAuth0IdState;
