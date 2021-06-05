import '@emotion/react';
import EmotionThemeColors from './theme/EmotionThemeColors';
import { EmotionThemeSizes } from './theme/EmotionThemeSizes';

declare module '@emotion/react' {
  export interface Theme {
    color: EmotionThemeColors;
    size: typeof EmotionThemeSizes;
  }
}
