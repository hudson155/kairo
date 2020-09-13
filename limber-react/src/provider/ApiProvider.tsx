import React, { useContext } from 'react';
import { LimberApi } from '../api/LimberApi';
import { useAuth0 } from '@auth0/auth0-react';
import { environment } from '../environment';

const Context = React.createContext<LimberApi>(undefined as unknown as LimberApi);

const ApiProvider: React.FC = (props) => {
  const auth = useAuth0();
  const api = new LimberApi(environment.REACT_APP_LIMBER_API_BASE_URL, auth.getAccessTokenSilently);
  return (
    <Context.Provider value={api}>
      {props.children}
    </Context.Provider>
  );
};

export default ApiProvider;

export const useApi = () => useContext(Context);
