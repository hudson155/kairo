import React, { useContext } from 'react';
import env from '../env';

interface DebugSettings {
  readonly showDebugMessages: boolean;
  readonly additionalLimberApiLatencyMs: number;
}

const Context = React.createContext<DebugSettings>({
  showDebugMessages: env.SHOW_DEBUG_MESSAGES,
  additionalLimberApiLatencyMs: env.ADDITIONAL_LIMBER_API_LATENCY_MS,
});

export const useDebugSettings = (): DebugSettings => useContext(Context);
