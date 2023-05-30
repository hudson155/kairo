import { organizationApiState } from 'api/OrganizationApi';
import { atom, selector } from 'recoil';
import OrganizationRep from 'rep/OrganizationRep';

const organizationsState = atom<Map<string, OrganizationRep>>({
  key: 'admin/organizations',
  default: selector<Map<string, OrganizationRep>>({
    key: 'admin/organizations-default',
    get: async ({ get }) => {
      const organizationApi = get(organizationApiState);
      const organizations = await organizationApi.listAll();
      return new Map(organizations.map((organization) => [organization.id, organization]));
    },
  }),
});

export default organizationsState;
