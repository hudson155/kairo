import '@emotion/react';
import EmotionThemeColors from './theme/EmotionThemeColors';
import { EmotionThemeSizes } from './theme/EmotionThemeSizes';
import { EmotionThemeUtil } from './theme/EmotionThemeUtil';
import { EmotionThemeZIndices } from './theme/EmotionThemeZIndices';

declare module '@emotion/react' {
  export interface Theme {
    color: EmotionThemeColors;
    size: typeof EmotionThemeSizes;
    util: typeof EmotionThemeUtil;
    zIndex: typeof EmotionThemeZIndices;
  }
}
