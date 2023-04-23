import { IdToken } from '@auth0/auth0-spa-js';
import { selector } from 'recoil';
import auth0ClientState from 'state/global/auth/auth0Client';

const auth0IdTokenState = selector<IdToken | undefined>({
  key: 'auth/auth0IdToken',
  get: async ({ get }) => {
    const auth0 = get(auth0ClientState);
    return await auth0.getIdTokenClaims();
  },
  // The Auth0 client has its own cache.
  cachePolicy_UNSTABLE: { eviction: 'most-recent' }, // eslint-disable-line @typescript-eslint/naming-convention
});

export default auth0IdTokenState;
