/** @jsxImportSource @emotion/react */

import { useAuth0 } from '@auth0/auth0-react';
import { CSSObject } from '@emotion/react';
import React from 'react';
import { Link } from 'react-router-dom';
import { signInPageDescriptor } from '../../pages/SignInPage/SignInPage';
import { signOutPagePath } from '../../pages/SignOutPage/SignOutPage';

const styles = {
  root: (theme: any): CSSObject => ({
    backgroundColor: theme.color.grey50,
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    padding: theme.size.$16,
    boxShadow: `0 0 ${theme.size.$4} ${theme.color.grey600}`,
    fontSize: theme.size.$16,
    fontWeight: 'bold',
  }),
  left: (theme: any): CSSObject => ({
    flexGrow: 1,
    'a:not(:last-child)': {
      marginRight: theme.size.$12,
    },
  }),
  right: (theme: any): CSSObject => ({
    flexDirection: 'row-reverse',
    'a:not(:last-child)': {
      marginRight: theme.size.$12,
    },
  }),
};

const TopLevelNavbar: React.FC = () => {
  const auth = useAuth0();

  return (
    <header css={styles.root}>
      <ul css={styles.left} />
      <ul css={styles.right}>
        {auth.isAuthenticated
          ? <Link to={signOutPagePath()}>Sign out</Link>
          : <Link to={signInPageDescriptor()}>Sign in</Link>
        }
      </ul>
    </header>
  );
};

export default TopLevelNavbar;
