import { organizationAuthApiState } from 'api/OrganizationAuthApi';
import { atomFamily, selector } from 'recoil';
import OrganizationAuthRep from 'rep/OrganizationAuthRep';

const organizationAuthsState = atomFamily<OrganizationAuthRep | undefined, string>({
  key: 'core/organizationAuths',
  default: (authGuid) => selector<OrganizationAuthRep | undefined>({
    key: `core/organizationAuths-default-${authGuid}`,
    get: async ({ get }) => {
      const authApi = get(organizationAuthApiState({ authenticated: false }));
      return await authApi.get(authGuid) ?? undefined;
    },
  }),
});

export default organizationAuthsState;
