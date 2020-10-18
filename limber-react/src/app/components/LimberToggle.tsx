import { CSSObject } from '@emotion/core';
import React, { ReactElement } from 'react';

import { EmotionTheme } from '../EmotionTheme';

const styles = {
  toggle: (theme: EmotionTheme): CSSObject => ({
    position: 'relative',
    cursor: 'pointer',
    height: theme.size.$16,
    width: theme.size.$32,
    padding: theme.size.$2,
    borderRadius: theme.size.$16,
    background: theme.colors.grey400,
  }),
  toggleEnabled: (theme: EmotionTheme): CSSObject => ({
    background: theme.colors.blue400,
  }),
  slider: (theme: EmotionTheme): CSSObject => ({
    position: 'absolute',
    backgroundColor: theme.colors.grey50,
    height: theme.size.$16,
    width: theme.size.$16,
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
