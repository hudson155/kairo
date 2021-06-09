import EmotionThemeAppColors, {
  EmotionThemeAppColorsDark,
  EmotionThemeAppColorsLight,
} from './EmotionThemeAppColors';
import EmotionThemeThemeColors, {
  EmotionThemeThemeColorsDark,
  EmotionThemeThemeColorsLight,
} from './EmotionThemeThemeColors';

export default interface EmotionThemeColors {
  theme: EmotionThemeThemeColors,
  app: EmotionThemeAppColors,
}

export const EmotionThemeColorsDark: EmotionThemeColors = {
  theme: EmotionThemeThemeColorsDark,
  app: EmotionThemeAppColorsDark,
};

export const EmotionThemeColorsLight: EmotionThemeColors = {
  theme: EmotionThemeThemeColorsLight,
  app: EmotionThemeAppColorsLight,
};
