import { organizationAuthApiState } from 'api/OrganizationAuthApi';
import { getRootHost } from 'metadata';
import { atom, selector } from 'recoil';
import OrganizationAuthRep from 'rep/OrganizationAuthRep';

const organizationAuthState = atom<OrganizationAuthRep>({
  key: 'core/organizationAuth',
  default: selector({
    key: 'core/organizationAuth-default',
    get: async ({ get }) => {
      const authApi = get(organizationAuthApiState);
      const auth = await authApi.getByHostname(getRootHost());
      if (!auth) throw new Error('No auth found.');
      return auth;
    },
  }),
});

export default organizationAuthState;
