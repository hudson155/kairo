import React, { useEffect, useState } from 'react';
import { TenantRepComplete } from '../rep/Tenant';
import { LimberApi } from '../api/LimberApi';
import { environment } from '../environment';
import { BrowserRouter } from 'react-router-dom';
import AppRootRouter from './AppRootRouter';
import { Auth0Provider } from '@auth0/auth0-react';
import { app } from '../app';
import ApiProvider from '../provider/ApiProvider';
import LoadingPage from './components/LoadingPage/LoadingPage';

const api = new LimberApi(environment.REACT_APP_LIMBER_API_BASE_URL, () => Promise.resolve(undefined));

const App: React.FC = () => {
  const [tenant, setTenant] = useState<TenantRepComplete>();

  useEffect(() => {
    (async () => setTenant(await api.getTenant(app.rootDomain)))();
  }, []);

  if (!tenant) return <LoadingPage message="Loading tenant..." />;

  return (
    <Auth0Provider
      domain={environment.AUTH0_DOMAIN}
      clientId={tenant.auth0ClientId}
      redirectUri={app.rootUrl}
      audience={`https://${environment.AUTH0_DOMAIN}/api/v2/`}
    >
      <ApiProvider>
        <BrowserRouter>
          <AppRootRouter orgGuid={tenant.orgGuid} />
        </BrowserRouter>
      </ApiProvider>
    </Auth0Provider>
  );
};

export default App;
