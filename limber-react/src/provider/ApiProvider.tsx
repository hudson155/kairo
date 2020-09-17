import React, { useContext } from 'react';
import { LimberApi } from '../api/LimberApi';
import { useAuth0 } from '@auth0/auth0-react';
import { env } from '../env';

const Context = React.createContext<LimberApi>(undefined as unknown as LimberApi);

const ApiProvider: React.FC = (props) => {
  const auth = useAuth0();
  const api = new LimberApi(env.LIMBER_API_BASE_URL, auth.getAccessTokenSilently);
  return (
    <Context.Provider value={api}>
      {props.children}
    </Context.Provider>
  );
};

export default ApiProvider;

export const useApi = () => useContext(Context);
