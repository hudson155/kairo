/** @jsxImportSource @emotion/react */

import { CSSObject, Theme } from '@emotion/react';
import React from 'react';

const styles = {
  subnavItem: (theme: Theme): CSSObject => ({
    padding: `${theme.size.$4} ${theme.size.$6}`,
    ':hover': {
      backgroundColor: theme.color.theme.blue200,
    },
  }),
};

const SettingsSubnavItem: React.FC = ({ children }) => {
  return <div css={styles.subnavItem}>{children}</div>;
};

export default SettingsSubnavItem;
