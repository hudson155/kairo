import { AppState, Auth0Provider } from '@auth0/auth0-react';
import React, { ReactNode } from 'react';
import { useHistory } from 'react-router-dom';
import app from '../../app';
import env from '../../env';
import { useTenant } from '../../provider/TenantProvider';
import InnerAuthProvider from './InnerAuthProvider';

interface Props {
  readonly fallback: ReactNode;
}

/**
 * Enables interaction with Auth0. See
 * https://auth0.com/blog/complete-guide-to-react-user-authentication/ and other Auth0
 * documentation.
 */
const AuthProvider: React.FC<Props> = ({ fallback, children }) => {
  const history = useHistory();
  const tenant = useTenant();

  const onRedirectCallback = (appState?: AppState) => {
    history.replace(appState?.returnTo ?? window.location.pathname);
  };

  return (
    <Auth0Provider
      audience={`https://${env.AUTH0_DOMAIN}/api/v2/`}
      clientId={env.AUTH0_CLIENT_ID}
      organization={tenant.auth0OrgId}
      domain={env.AUTH0_DOMAIN}
      onRedirectCallback={onRedirectCallback}
      redirectUri={app.rootUrl}
    >
      <InnerAuthProvider fallback={fallback}>{children}</InnerAuthProvider>
    </Auth0Provider>
  );
};

export default AuthProvider;
