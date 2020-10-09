import { CSSObject } from '@emotion/core';
import React, { ReactElement } from 'react';

import { EmotionThemeType } from '../EmotionTheme';

type LimberButtonType = 'PRIMARY' | 'SECONDARY';

interface Props {
  readonly disabled?: boolean;
  readonly label: string;
  readonly type: LimberButtonType;
  readonly onClick: () => void;
}

const styles = {
  baseButton: (theme: EmotionThemeType): CSSObject => ({
    fontWeight: 'bold',
    fontSize: 'initial',
    border: 'none',
    borderRadius: theme.size.$4,
    padding: `${theme.size.$6} ${theme.size.$12} ${theme.size.$6} ${theme.size.$12}`,
  }),
  primary: (theme: EmotionThemeType): CSSObject => ({
    backgroundColor: theme.colors.blue400,
    color: theme.colors.grey100,
    outlineColor: theme.colors.blue500,
    ':disabled': {
      backgroundColor: theme.colors.blue200,
      color: theme.colors.grey100,
    },
  }),
  secondary: (theme: EmotionThemeType): CSSObject => ({
    backgroundColor: theme.colors.grey300,
    color: theme.colors.grey800,
    outlineColor: theme.colors.grey200,
    ':disabled': {
      backgroundColor: theme.colors.grey200,
      color: theme.colors.grey500,
    },
  }),
};

function LimberButton(props: Props): ReactElement {
  return (
    <button
      css={theme => [
        styles.baseButton(theme),
        props.type === 'PRIMARY' && styles.primary(theme),
        props.type === 'SECONDARY' && styles.secondary(theme),
      ]}
      disabled={props.disabled}
      onClick={props.onClick}
    >
      {props.label}
    </button>
  );
}

export default LimberButton;
