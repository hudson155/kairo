import { useAuth0 } from '@auth0/auth0-react';
import React from 'react';
import { NavLink } from 'react-router-dom';
import { signInPageDescriptor } from '../../pages/SignInPage';
import { signOutPagePath } from '../../pages/SignOutPage';

const SignInSignOutNavLink: React.FC = () => {
  const auth = useAuth0();

  if (auth.isAuthenticated) {
    return <NavLink to={signOutPagePath()}>Sign out</NavLink>;
  } else {
    return <NavLink to={signInPageDescriptor()}>Sign in</NavLink>;
  }
};

export default SignInSignOutNavLink;
