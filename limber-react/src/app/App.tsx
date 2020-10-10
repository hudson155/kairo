import { Auth0Provider } from '@auth0/auth0-react';
import React, { ReactElement, useEffect, useState } from 'react';
import { RelayEnvironmentProvider } from 'react-relay/hooks';
import { BrowserRouter } from 'react-router-dom';

import { LimberApi } from '../api/LimberApi';
import { app } from '../app';
import { env } from '../env';
import ApiProvider from '../provider/ApiProvider';
import LimberThemeProvider from '../provider/LimberThemeProvider';
import { environment as relayEnvironment } from '../relay-env';
import { TenantRepComplete } from '../rep/Tenant';

import AppRootRouter from './AppRootRouter';
import LoadingPage from './pages/LoadingPage/LoadingPage';

const api = new LimberApi(env.LIMBER_API_BASE_URL, () => Promise.resolve(undefined));

function App(): ReactElement {
  const [tenant, setTenant] = useState<TenantRepComplete>();

  useEffect(() => {
    (async () => setTenant(await api.getTenant(app.rootDomain)))();
  }, []);

  return (
    <LimberThemeProvider>
      {tenant == null ? <LoadingPage message="Loading tenant..." /> : (
        <Auth0Provider
          audience={`https://${env.AUTH0_DOMAIN}/api/v2/`}
          clientId={tenant.auth0ClientId}
          domain={env.AUTH0_DOMAIN}
          redirectUri={app.rootUrl}
        >
          <ApiProvider>
            <RelayEnvironmentProvider environment={relayEnvironment}>
              <BrowserRouter>
                <AppRootRouter tenant={tenant} />
              </BrowserRouter>
            </RelayEnvironmentProvider>
          </ApiProvider>
        </Auth0Provider>
      )}
    </LimberThemeProvider>
  );
}

export default App;
