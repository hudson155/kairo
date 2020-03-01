import React from 'react';
import { useAuth0 } from '../../../react-auth0-wrapper';
import { useHistory } from 'react-router';
import { rootUrl } from '../../../index';

const SignOutRedirector: React.FC = () => {
  const auth0 = useAuth0();
  const history = useHistory();

  if (history.action !== 'POP') {
    auth0.logout({ returnTo: rootUrl });
  }

  return null;
};

export default SignOutRedirector;
