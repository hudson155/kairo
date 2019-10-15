import React from 'react';
import { useAuth0 } from '../../../react-auth0-wrapper';

const SignOutPage: React.FC = () => {
  const { logout: signOut } = useAuth0();

  signOut();

  return <p>Signing out...</p>;
};

export default SignOutPage;
