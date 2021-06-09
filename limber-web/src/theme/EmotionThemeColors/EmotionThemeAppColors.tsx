/**
 * These colors are specific to the app. Some are defined, and some are based on the theme. In the
 * future, this could be config-based.
 */
import EmotionThemeThemeColors, {
  EmotionThemeThemeColorsDark,
  EmotionThemeThemeColorsLight,
} from './EmotionThemeThemeColors';

export default interface EmotionThemeAppColors {
  navigationHighlight: string;
  text: {
    normal: string;
  },
  background: {
    normal: string;
    accented: string;
    accentedHover: string;
    placeholderElement: string;
  };
  border: {
    normal: string;
  };
  boxShadow: {
    normal: string;
    mild: string;
  };
}

export const EmotionThemeAppColorsDark: EmotionThemeAppColors = appColors(EmotionThemeThemeColorsDark);

export const EmotionThemeAppColorsLight: EmotionThemeAppColors = appColors(EmotionThemeThemeColorsLight);

function appColors(theme: EmotionThemeThemeColors): EmotionThemeAppColors {
  /**
   * These could be theme-dependent.
   */
  const custom = {
    navigationHighlight: '#CD8E72',
  };

  return {
    navigationHighlight: custom.navigationHighlight,
    text: {
      normal: theme.grey800,
    },
    background: {
      normal: theme.grey100,
      accented: theme.grey50,
      accentedHover: theme.grey100,
      placeholderElement: theme.grey300,
    },
    border: {
      normal: theme.grey600,
    },
    boxShadow: {
      normal: theme.grey600,
      mild: theme.grey400,
    },
  };
}
