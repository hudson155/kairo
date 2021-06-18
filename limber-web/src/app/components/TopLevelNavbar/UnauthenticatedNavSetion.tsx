import React from 'react';
import { NavLink } from 'react-router-dom';
import { signInPageDescriptor } from '../../pages/SignInPage';
import NavbarItem from './NavbarItem';

const UnauthenticatedNavSection: React.FC = () => (
  <NavbarItem>
    <NavLink to={signInPageDescriptor()}>Sign in</NavLink>
  </NavbarItem>
);

export default UnauthenticatedNavSection;
