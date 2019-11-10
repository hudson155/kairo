import React from 'react';
import { useAuth0 } from '../../../react-auth0-wrapper';

const SignOutPage: React.FC = () => {
  const { logout: signOut } = useAuth0();

  signOut({ returnTo: process.env['REACT_APP_ROOT_URL'] });

  return <p>Signing out...</p>;
};

export default SignOutPage;
