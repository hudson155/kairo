import { useAuth0 } from '@auth0/auth0-react';
import { ReactElement } from 'react';

function SignInPage(): ReactElement | null {
  const auth = useAuth0();
  auth.loginWithRedirect();
  return null;
}

export default SignInPage;
