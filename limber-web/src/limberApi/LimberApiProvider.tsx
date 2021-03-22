import React, { useContext } from 'react';
import env from '../env';
import { useDebugSettings } from '../provider/DebugSettingsProvider';
import LimberApi from './LimberApi';

const Context = React.createContext<LimberApi>(undefined as unknown as LimberApi);

export const UnauthenticatedLimberApiProvider: React.FC = ({ children }) => {
  const { additionalLimberApiLatencyMs } = useDebugSettings();

  const api = new LimberApi(env.LIMBER_API_BASE_URL, () => Promise.resolve(undefined), additionalLimberApiLatencyMs);

  return <Context.Provider value={api}>{children}</Context.Provider>;
};

export const useLimberApi = (): LimberApi => useContext(Context);
