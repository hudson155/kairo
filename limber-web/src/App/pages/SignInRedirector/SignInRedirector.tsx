import React from 'react';
import { useHistory } from 'react-router-dom';
import { useAuth } from '../../useAuth';

const SignInRedirector: React.FC = () => {
  const auth = useAuth();
  const history = useHistory();

  if (history.action !== 'POP') {
    auth.login();
  }

  return null;
};

export default SignInRedirector;
