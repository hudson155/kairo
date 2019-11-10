import React from 'react';
import { useAuth0 } from '../../../react-auth0-wrapper';

const SignInRedirector: React.FC = () => {
  const auth0 = useAuth0();

  auth0.loginWithRedirect();

  return null;
};

export default SignInRedirector;
