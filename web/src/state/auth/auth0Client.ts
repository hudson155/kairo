import { Auth0Client, createAuth0Client } from '@auth0/auth0-spa-js';
import env from 'env';
import { rootUrl } from 'metadata';
import { selector } from 'recoil';
import organizationAuth0IdState from 'state/auth/organizationId';

const auth0ClientState = selector<Auth0Client>({
  key: 'auth/auth0Client',
  get: async ({ get }) =>
    await createAuth0Client({
      authorizationParams: {
        audience: `https://${env.auth0.domain}/api/v2/`,
        organization: get(organizationAuth0IdState),
        redirect_uri: rootUrl, // eslint-disable-line @typescript-eslint/naming-convention
      },
      domain: env.auth0.domain,
      clientId: env.auth0.clientId,
    }),
  dangerouslyAllowMutability: true,
});

export default auth0ClientState;
