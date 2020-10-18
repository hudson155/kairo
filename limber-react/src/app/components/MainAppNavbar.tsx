import { CSSObject } from '@emotion/core';
import React, { ReactElement } from 'react';

import { useLimberTheme } from '../../provider/LimberThemeProvider';
import limberLogo from '../../resources/images/limber_logo.png';
import { EmotionTheme } from '../EmotionTheme';

import LimberToggle from './LimberToggle';

const styles = {
  root: (theme: EmotionTheme): CSSObject => ({
    backgroundColor: theme.colors.grey50,
    color: theme.colors.grey800,
    height: theme.size.$32,
    fontSize: theme.size.$16,
    fontStyle: 'bold',
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    padding: `${theme.size.$16}`,
    boxShadow: `0px 0px ${theme.size.$4} ${theme.colors.grey600}`,
  }),
  logo: (theme: EmotionTheme): CSSObject => ({
    height: 'inherit',
    marginRight: theme.size.$12,
  }),
  tabs: {
    flexGrow: 1,
  },
  darkModeToggle: (theme: EmotionTheme): CSSObject => ({
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    '> span': {
      paddingRight: theme.size.$8,
    },
  }),
};

// TODO (ENG-85): Implement this skeleton nav bar
function MainAppNavbar(): ReactElement {
  const { isLightTheme, setIsLightTheme } = useLimberTheme();

  return (
    <div css={theme => styles.root(theme)}>
      <img alt="logo" css={styles.logo} src={String(limberLogo)} />
      <div css={styles.tabs} />
      <div css={theme => styles.darkModeToggle(theme)}>
        <span>Dark mode</span>
        <LimberToggle enabled={!isLightTheme} onToggle={enabled => setIsLightTheme(!enabled)} />
      </div>
    </div>);
}

export default MainAppNavbar;
