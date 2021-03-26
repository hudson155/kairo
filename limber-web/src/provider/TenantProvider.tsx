import React, { ReactNode, useContext, useEffect } from 'react';
import app from '../app';
import { useLimberApi } from '../limberApi/LimberApiProvider';
import TenantRep from '../rep/TenantRep';
import useLoadingState from '../util/LoadingState';
import { checkNotUndefined } from '../util/Util';

const Context = React.createContext<TenantRep>(undefined as unknown as TenantRep);

interface TenantProviderProps {
  readonly fallback: ReactNode;
}

const TenantProvider: React.FC<TenantProviderProps> = ({ fallback, children }) => {
  const api = useLimberApi();

  const [tenant, setTenant, setError] = useLoadingState<TenantRep | undefined>();

  useEffect(() => {
    api.tenant.getByDomain(app.rootDomain).then(setTenant, setError);
  }, []);

  if (tenant.isLoading) return <>{fallback}</>;

  const value = checkNotUndefined(tenant.get(), 'Tenant not found.');
  return <Context.Provider value={value}>{children}</Context.Provider>;
};

export default TenantProvider;

export const useTenant = (): TenantRep => useContext(Context);
