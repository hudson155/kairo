import EmotionThemeColors, {
  EmotionThemeColorsDark,
  EmotionThemeColorsLight,
} from './EmotionThemeColors';
import { EmotionThemeSizes } from './EmotionThemeSizes';

export default interface EmotionTheme {
  color: EmotionThemeColors;
  size: typeof EmotionThemeSizes;
}

const EmotionThemeDark: EmotionTheme = {
  color: EmotionThemeColorsDark,
  size: EmotionThemeSizes,
};

const EmotionThemeLight: EmotionTheme = {
  color: EmotionThemeColorsLight,
  size: EmotionThemeSizes,
};

export {
  EmotionThemeDark,
  EmotionThemeLight,
};
