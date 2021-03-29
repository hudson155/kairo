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
}

export default LimberApiProvider;

export const useLimberApi = (): LimberApi => useContext(Context);
