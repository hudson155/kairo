import { selector } from 'recoil';
import organizationState from 'state/core/organization';

const organizationNameState = selector<string>({
  key: `core/organizationName`,
  get: ({ get }) => {
    const organization = get(organizationState);
    return organization.name;
  },
});

export default organizationNameState;
