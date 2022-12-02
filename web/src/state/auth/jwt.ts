import { selector } from 'recoil';
import auth0ClientState from 'state/auth/auth0Client';

const jwtState = selector<string>({
  key: 'auth/jwt',
  get: async ({ get }) => {
    const auth0Client = get(auth0ClientState);
    return await auth0Client.getTokenSilently();
  },
  // The Auth0 client has its own cache.
  cachePolicy_UNSTABLE: { eviction: 'most-recent' }, // eslint-disable-line @typescript-eslint/naming-convention
});

export default jwtState;
