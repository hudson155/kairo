import React from 'react';
import { useAuth0 } from '../../../react-auth0-wrapper';

const SignInPage: React.FC = () => {
  const { loginWithRedirect: signIn } = useAuth0();

  signIn();

  return <p>Signing in...</p>;
};

export default SignInPage;
