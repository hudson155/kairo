import { useAuth0 } from '@auth0/auth0-react';
import { ClassNames, CSSObject } from '@emotion/core';
import React, { ReactElement } from 'react';
import { NavLink, useLocation } from 'react-router-dom';

import { useLimberTheme } from '../../provider/LimberThemeProvider';
import { useOrg } from '../../provider/OrgProvider';
import { FeatureRepComplete } from '../../rep/Feature';
import limberLogo from '../../resources/images/limber_logo.png';
import { EmotionTheme } from '../EmotionTheme';

import LimberToggle from './LimberToggle';

const styles = {
  root: (theme: EmotionTheme): CSSObject => ({
    backgroundColor: theme.colors.grey50,
    color: theme.colors.grey800,
    height: theme.size.$32,
    fontSize: theme.size.$16,
    fontWeight: 'bold',
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
  right: (theme: EmotionTheme): CSSObject => ({
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    '> span': {
      paddingRight: theme.size.$8,
    },
  }),
  nav: (theme: EmotionTheme): CSSObject => ({
    flexGrow: 1,
    'a:not(:last-child)': {
      marginRight: theme.size.$12,
    },
  }),
  activeNavLink: (theme: EmotionTheme): CSSObject => ({
    borderBottom: `${theme.size.$2} solid ${theme.colors.special.copper}`,
  }),
  signIn: (): CSSObject => ({
    cursor: 'pointer',
  }),
};

function MainAppNavbarItem(props: {
  readonly feature: FeatureRepComplete;
}): ReactElement {
  return (
    <ClassNames>
      {({ css, theme }) => (
        <NavLink activeClassName={css(styles.activeNavLink(theme))} to={props.feature.path}>
          {props.feature.name}
        </NavLink>
      )}
    </ClassNames>
  );
}

function MainAppNavbarSettingsDropdown(): ReactElement {
  const { isLightTheme, setIsLightTheme } = useLimberTheme();
  return (
    <>
      <span>Dark mode</span>
      <LimberToggle enabled={!isLightTheme} onToggle={enabled => setIsLightTheme(!enabled)} />
    </>
  );
}

function MainAppNavbar(): ReactElement {
  const auth = useAuth0();
  const location = useLocation();
  const org = useOrg();

  const signIn = () => {
    auth.loginWithRedirect({ appState: { returnTo: location.pathname } });
  };

  return (
    <div css={theme => styles.root(theme)}>
      <img alt="logo" css={styles.logo} src={String(limberLogo)} />
      <ul css={theme => styles.nav(theme)}>
        {org && org.features.map(feature => (<MainAppNavbarItem feature={feature} key={feature.guid} />))}
      </ul>
      <div css={theme => styles.right(theme)}>
        {auth.isAuthenticated
          ? <MainAppNavbarSettingsDropdown />
          : <a css={styles.signIn()} onClick={signIn}>Sign in</a>
        }
      </div>
    </div>);
}

export default MainAppNavbar;
