import createAuth0Client, { Auth0Client } from '@auth0/auth0-spa-js';
import env from 'env';
import { rootUrl } from 'metadata';
import { selector } from 'recoil';

const auth0ClientState = selector<Auth0Client>({
  key: 'auth/auth0Client',
  get: async () =>
    await createAuth0Client({
      audience: `https://${env.auth0.domain}/api/v2/`,
      client_id: env.auth0.clientId,
      domain: env.auth0.domain,
      redirect_uri: rootUrl,
    }),
});

export default auth0ClientState;
