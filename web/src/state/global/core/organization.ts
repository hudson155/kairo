import { organizationApiState } from 'api/OrganizationApi';
import { atom, selector } from 'recoil';
import OrganizationRep from 'rep/OrganizationRep';
import organizationGuidState from 'state/global/core/organizationGuid';

const organizationState = atom<OrganizationRep>({
  key: 'core/organization',
  default: selector({
    key: 'core/organization-default',
    get: async ({ get }) => {
      const organizationApi = get(organizationApiState);
      const organizationGuid = get(organizationGuidState);
      const organization = await organizationApi.get(organizationGuid);
      if (!organization) throw new Error('No organization found.');
      return organization;
    },
  }),
});

export default organizationState;
