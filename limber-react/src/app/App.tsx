import React, { ReactElement, useEffect, useState } from 'react';
import { RelayEnvironmentProvider } from 'react-relay/hooks';
import { BrowserRouter } from 'react-router-dom';

import { LimberApi } from '../api/LimberApi';
import { app } from '../app';
import { env } from '../env';
import ApiProvider from '../provider/ApiProvider';
import AuthProvider from '../provider/AuthProvider';
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
      <BrowserRouter>
        {tenant == null ? <LoadingPage message="Loading tenant..." /> : (
          <AuthProvider tenant={tenant}>
            <ApiProvider>
              <RelayEnvironmentProvider environment={relayEnvironment}>
                <AppRootRouter tenant={tenant} />
              </RelayEnvironmentProvider>
            </ApiProvider>
          </AuthProvider>
        )}
      </BrowserRouter>
    </LimberThemeProvider>
  );
}

export default App;
