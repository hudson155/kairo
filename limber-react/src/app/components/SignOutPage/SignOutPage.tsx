import React from 'react';
import { useAuth0 } from '@auth0/auth0-react';
import { app } from '../../../app';

const SignOutPage: React.FC = () => {
  const auth = useAuth0();
  auth.logout({ returnTo: app.rootUrl });
  return null;
};

export default SignOutPage;
