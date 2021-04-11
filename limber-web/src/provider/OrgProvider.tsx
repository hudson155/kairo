import React, { ReactNode, useContext, useEffect } from 'react';
import { useLimberApi } from '../limberApi/LimberApiProvider';
import OrgRep from '../rep/OrgRep';
import useLoadingState from '../util/LoadingState';
import { checkNotUndefined } from '../util/Util';
import { useTenant } from './TenantProvider';

const Context = React.createContext<OrgRep.Complete>(
  undefined as unknown as OrgRep.Complete);

interface OrgProviderProps {
  readonly fallback: ReactNode;
}

const OrgProvider: React.FC<OrgProviderProps> = ({ fallback, children }) => {
  const api = useLimberApi();
  const tenant = useTenant();

  const [org, setOrg, setError] = useLoadingState<OrgRep.Complete | undefined>();

  useEffect(() => {
    api.org.get(tenant.orgGuid).then(setOrg, setError);
  }, [tenant.orgGuid]);

  if (org.isLoading) return <>{fallback}</>;

  const value = checkNotUndefined(org.get(), 'Org not found.');
  return <Context.Provider value={value}>{children}</Context.Provider>;
};

export default OrgProvider;

export const useOrg = (): OrgRep.Complete => useContext(Context);
