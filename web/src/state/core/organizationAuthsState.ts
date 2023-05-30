import { organizationAuthApiState } from 'api/OrganizationAuthApi';
import { atomFamily, selector } from 'recoil';
import OrganizationAuthRep from 'rep/OrganizationAuthRep';

const organizationAuthsState = atomFamily<OrganizationAuthRep | undefined, string>({
  key: 'core/organizationAuths',
  default: (organizationId) => selector<OrganizationAuthRep | undefined>({
    key: `core/organizationAuths-default-${organizationId}`,
    get: async ({ get }) => {
      const authApi = get(organizationAuthApiState({ authenticated: false }));
      return await authApi.get(organizationId) ?? undefined;
    },
  }),
});

export default organizationAuthsState;
