import { useAuth0 } from '../react-auth0-wrapper';
import { rootUrl } from '../index';

export function useAuth() {
  const mechanism = process.env['REACT_APP_AUTH_MECHANISM'];
  const auth0 = useAuth0();
  if (mechanism === 'LOCAL_STORAGE_JWT') {
    return {
      isLoading: () => false,
      isAuthenticated: () => Boolean(localStorage.getItem('jwt')),
      getTokenSilently: () => localStorage.getItem('jwt'),
      login: () => {
        // TODO: Redirect to rootUrl
      },
      logout: () => {
        localStorage.removeItem('jwt');
        // TODO: Redirect to rootUrl
      },
    };
  } else {
    return {
      isLoading: () => auth0.loading,
      isAuthenticated: () => auth0.isAuthenticated,
      getTokenSilently: () => auth0.getTokenSilently(),
      login: () => auth0.loginWithRedirect(),
      logout: () => auth0.logout({ returnTo: rootUrl }),
    };
  }
}
