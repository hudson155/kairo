import React from 'react';
import { useHistory } from 'react-router';
import { useAuth } from '../../useAuth';

const SignOutRedirector: React.FC = () => {
  const auth = useAuth();
  const history = useHistory();

  if (history.action !== 'POP') {
    auth.logout();
  }

  return null;
};

export default SignOutRedirector;
