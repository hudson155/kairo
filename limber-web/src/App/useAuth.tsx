import { rootUrl } from '../index';
import { useAuth0 } from '../react-auth0-wrapper';

interface Auth {
  isLoading: () => boolean;
  isAuthenticated: () => boolean;
  getTokenSilently: () => string;
  login: () => void;
  logout: () => void;
}

export function useAuth(): Auth {
  const mechanism = process.env['REACT_APP_AUTH_MECHANISM'];

  const auth0 = useAuth0();

  switch (mechanism) {
    case 'LOCAL_STORAGE_JWT':
      return localStorageAuth();
    default:
      return auth0Auth(auth0);
  }
}

function localStorageAuth(): Auth {
  return {
    isLoading: (): boolean => false,
    isAuthenticated: (): boolean => Boolean(localStorage.getItem('jwt')),
    getTokenSilently: (): string => {
      const result = localStorage.getItem('jwt');
      if (result === null) throw new Error('Cannot get null token.');
      return result;
    },
    login: (): void => {
    },
    logout: (): void => {
      localStorage.removeItem('jwt');
      window.location.assign(rootUrl);
    },
  };
}

// eslint-disable-next-line @typescript-eslint/no-explicit-any
function auth0Auth(auth0: any): Auth {
  return {
    isLoading: (): boolean => auth0.loading,
    isAuthenticated: (): boolean => auth0.isAuthenticated,
    getTokenSilently: (): string => auth0.getTokenSilently(),
    login: (): void => auth0.loginWithRedirect(),
    logout: (): void => auth0.logout({ returnTo: rootUrl }),
  };
}
