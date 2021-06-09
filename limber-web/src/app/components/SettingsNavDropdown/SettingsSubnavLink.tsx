/** @jsxImportSource @emotion/react */

import { CSSObject, Theme } from '@emotion/react';
import React from 'react';

const styles = {
  subnavLink: (theme: Theme): CSSObject => ({
    padding: theme.size.$4,
    ':hover': {
      backgroundColor: theme.color.theme.blue200,
    },
    '> *': {
      display: 'block',
    },
  }),
};

const SettingsSubnavLink: React.FC = ({ children }) => {
  return <div css={styles.subnavLink}>{children}</div>;
};

export default SettingsSubnavLink;
