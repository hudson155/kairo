import { selector } from 'recoil';
import organizationAuthState from 'state/core/organizationAuth';

const organizationIdState = selector<string>({
  key: 'core/organizationId',
  get: ({ get }) => {
    const auth = get(organizationAuthState);
    return auth.organizationId;
  },
});

export default organizationIdState;
