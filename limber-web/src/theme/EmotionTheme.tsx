import { Theme } from '@emotion/react';
import { EmotionThemeColorsDark, EmotionThemeColorsLight } from './EmotionThemeColors';
import { EmotionThemeSizes } from './EmotionThemeSizes';
import { EmotionThemeUtil } from './EmotionThemeUtil';
import { EmotionThemeZIndices } from './EmotionThemeZIndices';

export const EmotionThemeDark: Theme = {
  color: EmotionThemeColorsDark,
  size: EmotionThemeSizes,
  util: EmotionThemeUtil,
  zIndex: EmotionThemeZIndices,
};

export const EmotionThemeLight: Theme = {
  color: EmotionThemeColorsLight,
  size: EmotionThemeSizes,
  util: EmotionThemeUtil,
  zIndex: EmotionThemeZIndices,
};
