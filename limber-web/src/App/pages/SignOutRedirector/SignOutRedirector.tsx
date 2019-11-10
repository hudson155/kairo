import React from 'react';
import { useAuth0 } from '../../../react-auth0-wrapper';

const SignOutRedirector: React.FC = () => {
  const auth0 = useAuth0();

  auth0.logout({ returnTo: process.env['REACT_APP_ROOT_URL'] });

  return null;
};

export default SignOutRedirector;
