import React, { useContext } from 'react';
import OrgRep from '../../rep/OrgRep';

const Context = React.createContext<OrgRep.Complete>(
  undefined as unknown as OrgRep.Complete);

interface Props {
  readonly org: OrgRep.Complete;
}

const OrgProvider: React.FC<Props> = ({ org, children }) => {
  return <Context.Provider value={org}>{children}</Context.Provider>;
};

export default OrgProvider;

export const useOrg = (): OrgRep.Complete => useContext(Context);