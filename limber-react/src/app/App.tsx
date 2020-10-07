import { Auth0Provider } from '@auth0/auth0-react';
import { ThemeProvider } from 'emotion-theming';
import React, { ReactElement, useEffect, useState } from 'react';
import { RelayEnvironmentProvider } from 'react-relay/hooks';
import { BrowserRouter } from 'react-router-dom';

import { LimberApi } from '../api/LimberApi';
import { app } from '../app';
import { env } from '../env';
import ApiProvider from '../provider/ApiProvider';
import { environment as relayEnvironment } from '../relay-env';
import { TenantRepComplete } from '../rep/Tenant';

import AppRootRouter from './AppRootRouter';
import EmotionTheme from './EmotionTheme';
import LoadingPage from './pages/LoadingPage/LoadingPage';

const api = new LimberApi(env.LIMBER_API_BASE_URL, () => Promise.resolve(undefined));

function App(): ReactElement {
  const [tenant, setTenant] = useState<TenantRepComplete>();

  useEffect(() => {
    (async () => setTenant(await api.getTenant(app.rootDomain)))();
  }, []);

  // General whole app theming. Should occur before any components are returned.
  // TODO (ENG-82): Complete the base default theming
  document.body.style.backgroundColor = EmotionTheme.colors.grey100;

  return (
    <ThemeProvider theme={EmotionTheme}>
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
    </ThemeProvider>
  );
}

export default App;
