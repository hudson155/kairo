import { Auth0Client, createAuth0Client } from '@auth0/auth0-spa-js';
import env from 'env';
import { getRootUrl } from 'metadata';
import { selector } from 'recoil';
import organizationAuthState from 'state/core/organizationAuth';

const CODE_RE = /[?&]code=[^&]+/u;
const STATE_RE = /[?&]state=[^&]+/u;
const ERROR_RE = /[?&]error=[^&]+/u;

interface AppState {
  returnTo?: string;
}

const auth0ClientState = selector<Auth0Client>({
  key: 'auth/auth0Client',
  get: async ({ get }) => {
    const { auth0OrganizationId: organizationId } = get(organizationAuthState);
    const auth0 = await createClient(organizationId);
    if (hasAuthParams()) {
      await handleRedirectCallback(auth0);
    }
    return auth0;
  },
  dangerouslyAllowMutability: true,
});

export default auth0ClientState;

/**
 * Creates the Limber-configured version of {@link Auth0Client}.
 */
const createClient = async (organizationId: string): Promise<Auth0Client> => {
  return await createAuth0Client({
    authorizationParams: {
      audience: `https://${env.auth0.domain}/api/v2/`,
      organization: organizationId,
      redirect_uri: getRootUrl(), // eslint-disable-line @typescript-eslint/naming-convention
    },
    domain: env.auth0.domain,
    clientId: env.auth0.clientId,
  });
};

const hasAuthParams = (searchParams = window.location.search): boolean =>
  (CODE_RE.test(searchParams) || ERROR_RE.test(searchParams)) && STATE_RE.test(searchParams);

/**
 * After redirecting back from Auth0 upon login,
 * this function should be called to restore the {@link AppState}.
 */
const handleRedirectCallback = async (auth0Client: Auth0Client): Promise<void> => {
  let appState: AppState | undefined = undefined;
  try {
    ({ appState } = await auth0Client.handleRedirectCallback<AppState>());
  } catch (e) {
    console.error('Auth0 redirect callback failed.', e);
  }
  replaceState(appState?.returnTo);
};

const replaceState = (url?: string): void => {
  window.history.replaceState({}, document.title, url ?? window.location.pathname);
};
