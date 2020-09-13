import React from 'react';
import { useAuth0 } from '@auth0/auth0-react';

const SignInPage: React.FC = () => {
  const auth = useAuth0();
  auth.loginWithRedirect();
  return null;
};

export default SignInPage;
