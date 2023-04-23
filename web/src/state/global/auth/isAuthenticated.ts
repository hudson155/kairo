import { selector } from 'recoil';
import auth0ClientState from 'state/global/auth/auth0Client';

const isAuthenticatedState = selector<boolean>({
  key: 'auth/isAuthenticated',
  get: async ({ get }) => {
    const auth0 = get(auth0ClientState);
    return await auth0.isAuthenticated();
  },
  // The Auth0 client has its own cache.
  cachePolicy_UNSTABLE: { eviction: 'most-recent' }, // eslint-disable-line @typescript-eslint/naming-convention
});

export default isAuthenticatedState;
