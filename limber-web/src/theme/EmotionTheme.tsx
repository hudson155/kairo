import { Theme } from '@emotion/react';
import { EmotionThemeColorsDark, EmotionThemeColorsLight } from './EmotionThemeColors';
import { EmotionThemeSizes } from './EmotionThemeSizes';

const EmotionThemeDark: Theme = {
  color: EmotionThemeColorsDark,
  size: EmotionThemeSizes,
};

const EmotionThemeLight: Theme = {
  color: EmotionThemeColorsLight,
  size: EmotionThemeSizes,
};

export {
  EmotionThemeDark,
  EmotionThemeLight,
};
