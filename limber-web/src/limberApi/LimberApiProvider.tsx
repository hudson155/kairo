import React, { useContext } from 'react';
import { useDebugSettings } from '../provider/DebugSettingsProvider';
import LimberApi from './LimberApi';

const Context = React.createContext<LimberApi>(undefined as unknown as LimberApi);

export namespace LimberApiProvider {
  export const Unauthenticated: React.FC = ({ children }) => {
    const { additionalLimberApiLatencyMs } = useDebugSettings();

    const api = new LimberApi(() => Promise.resolve(undefined), additionalLimberApiLatencyMs);

    return <Context.Provider value={api}>{children}</Context.Provider>;
  };
}

export const useLimberApi = (): LimberApi => useContext(Context);
