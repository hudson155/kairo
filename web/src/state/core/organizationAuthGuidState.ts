import { selector } from 'recoil';
import organizationAuthState from 'state/core/organizationAuth';

const organizationAuthGuidState = selector<string>({
  key: 'core/organizationAuthGuid',
  get: ({ get }) => {
    const auth = get(organizationAuthState);
    return auth.guid;
  },
});

export default organizationAuthGuidState;
