import { useAuth0 } from '@auth0/auth0-react';
import React from 'react';
import app from '../../../app';
import RedirectingPage from '../RedirectingPage/RedirectingPage';
import { signedOutPagePath } from '../SignedOutPage/SignedOutPage';

export const signOutPagePath = () => '/sign-out';

/**
 * Redirects the browser to Auth0 to handle sign out.
 */
const SignOutPage: React.FC = () => {
  const auth = useAuth0();

  auth.logout({ returnTo: app.rootUrl + signedOutPagePath() });
  return <RedirectingPage debugMessage="Redirecting to Auth0 for sign out." />;
};

export default SignOutPage;