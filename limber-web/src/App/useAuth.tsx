import { rootUrl } from '../index';
import { useAuth0 } from '../react-auth0-wrapper';

export function useAuth() {
  const mechanism = process.env['REACT_APP_AUTH_MECHANISM'];

  const auth0 = useAuth0();

  switch (mechanism) {
    case 'LOCAL_STORAGE_JWT':
      return localStorageAuth();
    default:
      return auth0Auth(auth0);
  }
}

function localStorageAuth() {
  return {
    isLoading: () => false,
    isAuthenticated: () => Boolean(localStorage.getItem('jwt')),
    getTokenSilently: () => localStorage.getItem('jwt'),
    login: () => {
    },
    logout: () => {
      localStorage.removeItem('jwt');
      window.location.assign(rootUrl);
    },
  };
}

function auth0Auth(auth0: any) {
  return {
    isLoading: () => auth0.loading,
    isAuthenticated: () => auth0.isAuthenticated,
    getTokenSilently: () => auth0.getTokenSilently(),
    login: () => auth0.loginWithRedirect(),
    logout: () => auth0.logout({ returnTo: rootUrl }),
  };
}
