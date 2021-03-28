import { useAuth0 } from '@auth0/auth0-react';
import React from 'react';
import RedirectingPage from '../RedirectingPage/RedirectingPage';

export const signInPagePath = () => '/signin';

/**
 * Redirects the browser to Auth0 to handle signin.
 */
const SignInPage: React.FC = () => {
  const auth = useAuth0();

  // noinspection JSIgnoredPromiseFromCall
  auth.loginWithRedirect();
  return <RedirectingPage debugMessage="Redirecting to Auth0 for signin." />;
};

export default SignInPage;
