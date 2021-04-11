import { useAuth0 } from '@auth0/auth0-react';
import React, { useContext } from 'react';
import { useDebugSettings } from '../provider/DebugSettingsProvider';
import LimberApi from './LimberApi';

const Context = React.createContext<LimberApi>(
  undefined as unknown as LimberApi);

namespace LimberApiProvider {
  export const Unauthenticated: React.FC = ({ children }) => {
    const { additionalLimberApiLatencyMs } = useDebugSettings();

    const api = new LimberApi(() => Promise.resolve(undefined), additionalLimberApiLatencyMs);

    return <Context.Provider value={api}>{children}</Context.Provider>;
  };

  export const Authenticated: React.FC = ({ children }) => {
    const auth = useAuth0();
    const { additionalLimberApiLatencyMs } = useDebugSettings();

    const api = new LimberApi(auth.getAccessTokenSilently, additionalLimberApiLatencyMs);

    return <Context.Provider value={api}>{children}</Context.Provider>;
  };
}

export default LimberApiProvider;

export const useLimberApi = (): LimberApi => useContext(Context);
