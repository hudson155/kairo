import { organizationApiState } from 'api/OrganizationApi';
import { atom, selector } from 'recoil';
import OrganizationRep from 'rep/OrganizationRep';
import organizationIdState from 'state/core/organizationId';

const organizationState = atom<OrganizationRep>({
  key: 'core/organization',
  default: selector({
    key: 'core/organization-default',
    get: async ({ get }) => {
      const organizationApi = get(organizationApiState);
      const organizationId = get(organizationIdState);
      const organization = await organizationApi.get(organizationId);
      if (!organization) throw new Error('No organization found.');
      return organization;
    },
  }),
});

export default organizationState;
