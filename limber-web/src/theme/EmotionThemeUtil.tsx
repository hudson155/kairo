import { CSSObject } from '@emotion/react';

interface EmotionThemeUtilType {
  /**
   * Removes browser-default styling from button elements, allowing them to be used as semantic
   * buttons without picking up additional styling compared to a div.
   */
  buttonStyleReset: CSSObject,
}

export const EmotionThemeUtil: EmotionThemeUtilType = {
  buttonStyleReset: {
    padding: 'unset',
    backgroundColor: 'unset',
    color: 'unset',
    border: 'unset',
  },
};
