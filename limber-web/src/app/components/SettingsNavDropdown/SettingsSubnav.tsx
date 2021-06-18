/** @jsxImportSource @emotion/react */

import { CSSObject, Theme } from '@emotion/react';
import React, { useRef } from 'react';
import { NavLink } from 'react-router-dom';
import { useEscapeKeyListener, useOutsideClickListener } from '../../../hook/FilteredEventListener';
import { signOutPagePath } from '../../pages/SignOutPage';
import SettingsSubnavItem from './SettingsSubnavItem';
import SettingsSubnavSection from './SettingsSubnavSection';

const styles = {
  subnavContainer: (): CSSObject => ({
    height: 0,
    width: 0,
    alignSelf: 'flex-end',
    position: 'relative',
  }),
  subnav: (theme: Theme): CSSObject => {
    // The subnav uses some non-standard sizes. Most other components use theme sizes.
    return {
      position: 'absolute',
      top: theme.size.$12,
      right: 0,
      width: `224px`,
      zIndex: theme.zIndex.topLevelNavbarSubnav,
      backgroundColor: theme.color.app.background.accented,
      border: `${theme.size.$1} solid ${theme.color.app.border.normal}`,
      borderRadius: theme.size.borderRadius,
      overflow: 'hidden',
      '::after': {
        position: 'absolute',
        top: `-14px`,
        right: `22px`,
        border: `7px solid transparent`,
        borderBottomColor: theme.color.app.border.normal,
        display: 'inline-block',
        content: '""',
      },
    };
  },
  navLink: (): CSSObject => ({
    display: 'block',
  }),
};

interface Props {
  onHide: () => void;
}

const SettingsSubnav: React.FC<Props> = ({ onHide }) => {
  const ref = useRef(null);
  useEscapeKeyListener(onHide);
  useOutsideClickListener(ref, onHide);

  return (
    <div ref={ref} css={styles.subnavContainer}>
      <div css={styles.subnav}>
        <SettingsSubnavSection>
          <SettingsSubnavItem>
            <NavLink to={signOutPagePath()} css={styles.navLink}>Sign out</NavLink>
          </SettingsSubnavItem>
        </SettingsSubnavSection>
      </div>
    </div>
  );
};

export default SettingsSubnav;
