import { useAuth0 } from '@auth0/auth0-react';
import React from 'react';
import app from '../../../app';
import RedirectingPage from '../RedirectingPage/RedirectingPage';

export const signOutPagePath = () => '/signout';

/**
 * Redirects the browser to Auth0 to handle signout.
 */
const SignOutPage: React.FC = () => {
  const auth = useAuth0();

  auth.logout({ returnTo: app.rootUrl });
  return <RedirectingPage debugMessage="Redirecting to Auth0 for signout." />;
};

export default SignOutPage;
