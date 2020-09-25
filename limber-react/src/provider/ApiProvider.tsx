import { useAuth0 } from '@auth0/auth0-react';
import React, { ReactElement, ReactNode, useContext } from 'react';

import { LimberApi } from '../api/LimberApi';
import { env } from '../env';

interface Props {
  readonly children: ReactNode;
}

const Context = React.createContext<LimberApi>(undefined as unknown as LimberApi);

function ApiProvider(props: Props): ReactElement {
  const auth = useAuth0();
  const api = new LimberApi(env.LIMBER_API_BASE_URL, auth.getAccessTokenSilently);
  return (
    <Context.Provider value={api}>
      {props.children}
    </Context.Provider>
  );
}

export default ApiProvider;

export const useApi = (): LimberApi => useContext(Context);
