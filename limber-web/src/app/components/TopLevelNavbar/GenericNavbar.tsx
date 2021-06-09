/** @jsxImportSource @emotion/react */

import { CSSObject, Theme } from '@emotion/react';
import React, { ReactNode } from 'react';

const styles = {
  root: (theme: Theme): CSSObject => ({
    backgroundColor: theme.color.app.background.accented,
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    padding: theme.size.$16,
    boxShadow: `0 0 ${theme.size.boxShadow} ${theme.color.app.boxShadow.normal}`,
    fontSize: theme.size.$16,
    fontWeight: 'bold',
  }),
  left: (theme: Theme): CSSObject => ({
    flexGrow: 1,
    display: 'flex',
    alignItems: 'flex-start',
    '> *:not(:last-child)': {
      marginRight: theme.size.$12,
    },
  }),
  right: (theme: Theme): CSSObject => ({
    display: 'flex',
    alignItems: 'center',
    flexDirection: 'row',
    '> *:not(:last-child)': {
      marginRight: theme.size.$12,
    },
  }),
};

interface Props {
  left: ReactNode;
  right: ReactNode;
}

/**
 * Separates the generic navbar structure and styling from the top-level navbar content.
 */
const Navbar: React.FC<Props> = ({ left, right }) => (
  <header css={styles.root}>
    <ul css={styles.left}>{left}</ul>
    <ul css={styles.right}>{right}</ul>
  </header>
);

export default Navbar;
