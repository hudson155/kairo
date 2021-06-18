/** @jsxImportSource @emotion/react */

import { CSSObject, Theme } from '@emotion/react';
import React from 'react';

const styles = {
  subnavSection: (theme: Theme): CSSObject => ({
    ':not(:first-of-type)': {
      borderTop: `${theme.size.$1} solid ${theme.color.app.border.normal}`,
    },
  }),
};

const SettingsSubnavSection: React.FC = ({ children }) => {
  return <div css={styles.subnavSection}>{children}</div>;
};

export default SettingsSubnavSection;
