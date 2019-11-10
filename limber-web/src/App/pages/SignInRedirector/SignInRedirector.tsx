import React from 'react';
import { useAuth0 } from '../../../react-auth0-wrapper';
import { useHistory } from 'react-router-dom';

const SignInRedirector: React.FC = () => {
  const auth0 = useAuth0();
  const history = useHistory();

  if (history.action !== 'POP') {
    auth0.loginWithRedirect();
  }

  return null;
};

export default SignInRedirector;
