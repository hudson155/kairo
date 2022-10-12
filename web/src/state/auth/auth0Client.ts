import { Auth0Client, createAuth0Client } from '@auth0/auth0-spa-js';
import env from 'env';
import { rootUrl } from 'metadata';
import { selector } from 'recoil';

const auth0ClientState = selector<Auth0Client>({
  key: 'auth/auth0Client',
  get: async () =>
    await createAuth0Client({
      authorizationParams: {
        audience: `https://${env.auth0.domain}/api/v2/`,
        redirect_uri: rootUrl,
      },
      domain: env.auth0.domain,
      clientId: env.auth0.clientId,
    }),
  dangerouslyAllowMutability: true,
});

export default auth0ClientState;
