import { Auth0Provider } from '@auth0/auth0-react';
import { AppState } from '@auth0/auth0-react/dist/auth0-provider';
import React, { ReactNode } from 'react';
import { useHistory } from 'react-router-dom';

import { app } from '../app';
import { env } from '../env';
import { TenantRepComplete } from '../rep/Tenant';

interface Props {
  readonly children: ReactNode;
  readonly tenant: TenantRepComplete;
}

// From https://auth0.com/blog/complete-guide-to-react-user-authentication/
function AuthProvider(props: Props) {
  const history = useHistory();

  const onRedirectCallback = (appState: AppState) => {
    history.push(appState?.returnTo || window.location.pathname);
  };

  return (
    <Auth0Provider
      audience={`https://${env.AUTH0_DOMAIN}/api/v2/`}
      clientId={props.tenant.auth0ClientId}
      domain={env.AUTH0_DOMAIN}
      onRedirectCallback={onRedirectCallback}
      redirectUri={app.rootUrl}
    >
      {props.children}
    </Auth0Provider>
  );
}

export default AuthProvider;
