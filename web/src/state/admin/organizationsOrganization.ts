import { selectorFamily } from 'recoil';
import OrganizationRep from 'rep/OrganizationRep';
import organizationsState from 'state/admin/organizations';

const organizationsOrganizationState = selectorFamily<OrganizationRep | undefined, string>({
  key: 'admin/organizationsOrganization',
  get: (key) => ({ get }) => {
    const organizations = get(organizationsState);
    return organizations.get(key);
  },
});

export default organizationsOrganizationState;
