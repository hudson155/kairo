import env from 'env';

export interface DebugSettings {
  gitSha: string;
  showDebugMessages: boolean;
}

/**
 * Debug settings are currently hardcoded by environment (see the .env file), but this is structured
 * to look like a hook so that the settings could be configurable by user and/or stored in local
 * storage.
 */
export const useDebugSettings = (): DebugSettings => env.debug;
