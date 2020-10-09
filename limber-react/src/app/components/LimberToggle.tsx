import { CSSObject } from '@emotion/core';
import React, { ReactElement } from 'react';

import { EmotionThemeType } from '../EmotionTheme';

const styles = {
  toggle: (theme: EmotionThemeType): CSSObject => ({
    position: 'relative',
    cursor: 'pointer',
    height: theme.size.$24,
    width: theme.size.$48,
    padding: theme.size.$2,
    borderRadius: theme.size.$24,
    background: theme.colors.grey400,
  }),
  toggleEnabled: (theme: EmotionThemeType): CSSObject => ({
    background: theme.colors.blue400,
  }),
  slider: (theme: EmotionThemeType): CSSObject => ({
    position: 'absolute',
    backgroundColor: theme.colors.grey50,
    height: theme.size.$24,
    width: theme.size.$24,
    borderRadius: '100%',
    transition: 'transform 0.15s ease-in-out',
  }),
  sliderEnabled: (): CSSObject => ({
    transform: 'translate3d(100%, 0, 0)',
  })
};

interface Props {
  readonly enabled: boolean;
  readonly onToggle: (enabled: boolean) => void;
}

function LimberToggle(props: Props): ReactElement {
  return (
    <div
      css={theme => [styles.toggle(theme), props.enabled && styles.toggleEnabled(theme)]}
      onClick={() => props.onToggle(!props.enabled)}
    >
      <span css={theme => [styles.slider(theme), props.enabled && styles.sliderEnabled()]} />
    </div>
  );
}

export default LimberToggle;
