import React, { useEffect, useState } from 'react';
import { TenantRepComplete } from '../rep/Tenant';
import { LimberApi } from '../api/LimberApi';
import { env } from '../env';
import { BrowserRouter } from 'react-router-dom';
import AppRootRouter from './AppRootRouter';
import { Auth0Provider } from '@auth0/auth0-react';
import { app } from '../app';
import ApiProvider from '../provider/ApiProvider';
import LoadingPage from './components/LoadingPage/LoadingPage';
import { RelayEnvironmentProvider } from 'react-relay/hooks';
import {environment as relayEnvironment} from '../relay-env';

const api = new LimberApi(env.LIMBER_API_BASE_URL, () => Promise.resolve(undefined));

const App: React.FC = () => {
  const [tenant, setTenant] = useState<TenantRepComplete>();

  useEffect(() => {
    (async () => setTenant(await api.getTenant(app.rootDomain)))();
  }, []);

  if (!tenant) return <LoadingPage message="Loading tenant..." />;

  return (
    <Auth0Provider
      domain={env.AUTH0_DOMAIN}
      clientId={tenant.auth0ClientId}
      redirectUri={app.rootUrl}
      audience={`https://${env.AUTH0_DOMAIN}/api/v2/`}
    >
      <ApiProvider>
        <RelayEnvironmentProvider environment={relayEnvironment}>
          <BrowserRouter>
            <AppRootRouter orgGuid={tenant.orgGuid} />
          </BrowserRouter>
        </RelayEnvironmentProvider>
      </ApiProvider>
    </Auth0Provider>
  );
};

export default App;
