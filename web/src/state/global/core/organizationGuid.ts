import { selector } from 'recoil';
import organizationAuthState from 'state/global/core/organizationAuth';

const organizationGuidState = selector<string>({
  key: 'core/organizationGuid',
  get: ({ get }) => {
    const auth = get(organizationAuthState);
    return auth.organizationGuid;
  },
});

export default organizationGuidState;
