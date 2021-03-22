import React, { useContext, useState } from 'react';
import app from '../app';
import { useLimberApi } from '../limberApi/LimberApiProvider';
import { TenantRep } from '../rep/TenantRep';
import Readable from './Readable';

const Context = React.createContext<Readable<TenantRep | undefined>>(undefined as unknown as Readable<TenantRep | undefined>);

const TenantProvider: React.FC = ({ children }) => {
  const api = useLimberApi();

  const [tenant] = useState(new Readable(api.getTenant(app.rootDomain)));

  return <Context.Provider value={tenant}>{children}</Context.Provider>;
};

export default TenantProvider;

export const useTenant = (): Readable<TenantRep | undefined> => useContext(Context);
