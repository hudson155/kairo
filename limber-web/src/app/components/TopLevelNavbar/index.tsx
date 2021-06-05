/** @jsxImportSource @emotion/react */

import React from 'react';
import { useOrg } from '../../../provider/OrgProvider';
import FeatureNavLink from './FeatureNavLink';
import SignInSignOutNavLink from './SignInSignOutNavLink';
import styles from './styles';

/**
 * The navbar to show at the very top of all pages.
 *
 * TODO: Add some kind of a logo. Perhaps the organization logo.
 */
const TopLevelNavbar: React.FC = () => {
  const org = useOrg();

  return (
    <header css={styles.root}>
      <ul css={styles.left}>
        {org && org.features.map(feature => <FeatureNavLink feature={feature} />)}
      </ul>
      <ul css={styles.right}>
        <SignInSignOutNavLink />
      </ul>
    </header>
  );
};

export default TopLevelNavbar;
