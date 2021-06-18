import React from 'react';
import SettingsNavDropdown from '../SettingsNavDropdown';
import NavbarItem from './NavbarItem';

const AuthenticatedNavSection: React.FC = () => (
  <NavbarItem>
    <SettingsNavDropdown />
  </NavbarItem>
);

export default AuthenticatedNavSection;
