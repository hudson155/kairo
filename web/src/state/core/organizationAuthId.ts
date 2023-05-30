import { selector } from 'recoil';
import organizationAuthState from 'state/core/organizationAuth';

const organizationAuthIdState = selector<string>({
  key: 'core/organizationAuthId',
  get: ({ get }) => {
    const auth = get(organizationAuthState);
    return auth.id;
  },
});

export default organizationAuthIdState;
