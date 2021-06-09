import React, { ReactNode, useContext, useEffect } from 'react';
import app from '../app';
import useLoadingState from '../hook/LoadingState';
import { useLimberApi } from '../limberApi/LimberApiProvider';
import TenantRep from '../rep/TenantRep';
import { checkNotUndefined } from '../util/Util';

const Context = React.createContext<TenantRep.Complete>(
  undefined as unknown as TenantRep.Complete);

interface Props {
  readonly fallback: ReactNode;
}

/**
 * Provides the tenant, based on the app's root domain. The provided value will never change
 * throughout the app lifecycle.
 */
const TenantProvider: React.FC<Props> = ({ fallback, children }) => {
  const api = useLimberApi();

  const [tenant, setTenant, setError] = useLoadingState<TenantRep.Complete | undefined>();

  useEffect(() => {
    api.tenant.getByDomain(app.rootDomain).then(setTenant, setError);
  }, []);

  if (tenant.isLoading) return <>{fallback}</>;

  const value = checkNotUndefined(tenant.get(), 'Tenant not found.');
  return <Context.Provider value={value}>{children}</Context.Provider>;
};

export default TenantProvider;

export const useTenant = (): TenantRep.Complete => useContext(Context);
