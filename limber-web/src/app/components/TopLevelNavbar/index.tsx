/** @jsxImportSource @emotion/react */

import { useAuth0 } from '@auth0/auth0-react';
import React from 'react';
import { useOrg } from '../../../provider/AuthenticatedStateProvider/OrgProvider';
import AuthenticatedNavSection from './AuthenticatedNavSection';
import FeatureNavLink from './FeatureNavLink';
import Navbar from './GenericNavbar';
import UnauthenticatedNavSection from './UnauthenticatedNavSetion';

/**
 * The navbar to show at the very top of all pages. As it gets more complex it can be broken down
 * into more components.
 *
 * TODO: Add some kind of a logo. Perhaps the organization logo.
 *
 * TODO: Make this (and the settings nav dropdown) look good on mobile.
 */
const TopLevelNavbar: React.FC = () => {
  const auth = useAuth0();
  const org = useOrg();

  const left = org && org.features.map(feature => (
    <FeatureNavLink key={feature.guid} feature={feature} />
  ));

  const right = auth.isAuthenticated ? <AuthenticatedNavSection /> : <UnauthenticatedNavSection />;

  return <Navbar left={left} right={right} />;
};

export default TopLevelNavbar;
