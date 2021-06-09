import React from 'react';
import { NavLink } from 'react-router-dom';
import { signInPageDescriptor } from '../../pages/SignInPage';

const UnauthenticatedNavSection: React.FC = () => (
  <NavLink to={signInPageDescriptor()}>Sign in</NavLink>
);

export default UnauthenticatedNavSection;
