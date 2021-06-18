/** @jsxImportSource @emotion/react */

import { CSSObject, Theme } from '@emotion/react';
import React from 'react';

const styles = {
  item: (theme: Theme): CSSObject => ({
    marginRight: theme.size.$12,
  }),
};

const NavbarItem: React.FC = ({ children }) => {
  return <div css={styles.item}>{children}</div>;
};

export default NavbarItem;
