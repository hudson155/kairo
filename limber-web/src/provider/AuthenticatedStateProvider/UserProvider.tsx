import React, { useContext } from 'react';
import UserRep from '../../rep/UserRep';

const Context = React.createContext<UserRep.Complete>(
  undefined as unknown as UserRep.Complete);

interface Props {
  readonly user: UserRep.Complete;
}

const UserProvider: React.FC<Props> = ({ user, children }) => {
  return <Context.Provider value={user}>{children}</Context.Provider>;
};

export default UserProvider;

export const useUser = (): UserRep.Complete => useContext(Context);
