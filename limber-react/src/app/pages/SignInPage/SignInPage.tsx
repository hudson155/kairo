import { useAuth0 } from '@auth0/auth0-react';
import { Base64 } from 'js-base64';
import { ReactElement } from 'react';
import { useLocation } from 'react-router-dom';

const RETURN_TO_KEY = 'return_to';

function signInPagePath(returnTo?: string): string {
  const queryString = returnTo != null ? `?${RETURN_TO_KEY}=${Base64.encode(returnTo)}` : '';
  return `/signin${queryString}`;
}

function SignInPage(): ReactElement | null {
  const auth = useAuth0();
  const location = useLocation();

  const queryParams = new URLSearchParams(location.search);
  const returnTo = queryParams.get(RETURN_TO_KEY);

  auth.loginWithRedirect({ appState: { returnTo } });
  return null;
}

export { signInPagePath };
export default SignInPage;
