import env from '../env';

/**
 * Debug settings are currently hardcoded by environment (see the .env file), but this is structured
 * to look like a hook so that the settings could be configurable by user and/or stored in local
 * storage.
 */
const debugSettings = {
  showDebugMessages: env.SHOW_DEBUG_MESSAGES,
  additionalLimberApiLatencyMs: env.ADDITIONAL_LIMBER_API_LATENCY_MS,
};

export const useDebugSettings = () => debugSettings;
