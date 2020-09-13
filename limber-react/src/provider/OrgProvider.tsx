import React, { useContext } from 'react';
import { OrgRepComplete } from '../rep/Org';

const Context = React.createContext<OrgRepComplete>(
  undefined as unknown as OrgRepComplete,
);

const OrgProvider = Context.Provider;

export default OrgProvider;

export const useOrg = () => useContext(Context);
