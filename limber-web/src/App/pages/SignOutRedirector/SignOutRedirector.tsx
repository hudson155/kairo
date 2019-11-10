import React from 'react';
import { useAuth0 } from '../../../react-auth0-wrapper';
import { useHistory } from 'react-router';

const SignOutRedirector: React.FC = () => {
  const auth0 = useAuth0();
  const history = useHistory();

  if (history.action !== 'POP') {
    auth0.logout({ returnTo: process.env['REACT_APP_ROOT_URL'] });
  }

  return null;
};

export default SignOutRedirector;
