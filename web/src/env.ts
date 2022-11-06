/* eslint-disable @typescript-eslint/no-non-null-assertion */

import { DebugSettings } from 'hook/useDebugSettings';

interface Env {
  auth0: {
    clientId: string;
    domain: string;
  };
  debug: DebugSettings;
  limber: {
    apiBaseUrl: string;
  };
}

/**
 * Environment variables must be prefixed by REACT_APP.
 * Those passed via the command must also be prefixed.
 * However, Storybook only allows the STORYBOOK prefix, not REACT_APP.
 */
const env: Env = {
  auth0: {
    clientId: process.env.REACT_APP_AUTH0_CLIENT_ID!,
    domain: process.env.REACT_APP_AUTH0_DOMAIN!,
  },
  debug: {
    gitSha: (process.env.REACT_APP_DEBUG_GIT_SHA ?? process.env.STORYBOOK_DEBUG_GIT_SHA)!,
    showDebugMessages: JSON.parse(process.env.REACT_APP_DEBUG_SHOW_DEBUG_MESSAGES!) as boolean,
  },
  limber: {
    apiBaseUrl: process.env.REACT_APP_LIMBER_API_BASE_URL!,
  },
};

export default env;
