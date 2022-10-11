import { DebugSettings } from 'hook/useDebugSettings';

interface Env {
  debug: DebugSettings;
  limber: {
    apiBaseUrl: string;
  };
}

const env: Env = {
  debug: {
    gitSha: process.env.REACT_APP_DEBUG_GIT_SHA!,
    showDebugMessages: JSON.parse(process.env.REACT_APP_DEBUG_SHOW_DEBUG_MESSAGES!),
  },
  limber: {
    apiBaseUrl: process.env.REACT_APP_LIMBER_API_BASE_URL!,
  },
};

export default env;
