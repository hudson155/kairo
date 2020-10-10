// eslint-disable-next-line import/exports-last
export interface EmotionTheme {
  colors: {
    blue200: string;
    blue300: string;
    blue400: string;
    blue500: string;
    blue600: string;
    blue700: string;
    grey50: string;
    grey75: string;
    grey100: string;
    grey200: string;
    grey300: string;
    grey400: string;
    grey500: string;
    grey600: string;
    grey700: string;
    grey800: string;
    grey900: string;
    green400: string;
    green500: string;
    green600: string;
    green700: string;
    orange400: string;
    orange500: string;
    orange600: string;
    orange700: string;
    red400: string;
    red500: string;
    red600: string;
    red700: string;
  };
  size: {
    $2: string;
    $4: string;
    $6: string;
    $8: string;
    $12: string;
    $16: string;
    $24: string;
    $32: string;
    $48: string;
    $64: string;
  };
}

// Colors based on Adobe Spectrum: Dark https://spectrum.adobe.com/page/color/
const EmotionThemeColorsDark = {
  blue200: '#004DB8',
  blue300: '#0D67D2',
  blue400: '#2680eb',
  blue500: '#378ef0',
  blue600: '#4b9cf5',
  blue700: '#5aa9fa',
  green400: '#2d9d78',
  green500: '#33ab84',
  green600: '#39b990',
  green700: '#3fc89c',
  grey50: '#252525',
  grey75: '#2f2f2f',
  grey100: '#323232',
  grey200: '#3e3e3e',
  grey300: '#4a4a4a',
  grey400: '#5a5a5a',
  grey500: '#6e6e6e',
  grey600: '#909090',
  grey700: '#b9b9b9',
  grey800: '#e3e3e3',
  grey900: '#ffffff',
  orange400: '#e68619',
  orange500: '#f29423',
  orange600: '#f9a43f',
  orange700: '#ffb55b',
  red400: '#e34850',
  red500: '#ec5b62',
  red600: '#f76d74',
  red700: '#ff7b82',
};

// Colors based on Adobe Spectrum: Light https://spectrum.adobe.com/page/color/
const EmotionThemeColorsLight = {
  blue200: '#59B3FF',
  blue300: '#409AFF',
  blue400: '#2680EB',
  blue500: '#1473E6',
  blue600: '#0D66D0',
  blue700: '#095ABA',
  green400: '#2D9D78',
  green500: '#268E6C',
  green600: '#12805C',
  green700: '#107154',
  grey50: '#FFFFFF',
  grey75: '#FAFAFA',
  grey100: '#F5F5F5',
  grey200: '#EAEAEA',
  grey300: '#E1E1E1',
  grey400: '#CACACA',
  grey500: '#B3B3B3',
  grey600: '#8E8E8E',
  grey700: '#6E6E6E',
  grey800: '#4B4B4B',
  grey900: '#2C2C2C',
  orange400: '#E68619',
  orange500: '#DA7B11',
  orange600: '#CB6F10',
  orange700: '#BD640d',
  red400: '#E34850',
  red500: '#D7373F',
  red600: '#C9252D',
  red700: '#BB121A',
};

const EmotionThemeSizes = {
  $2: '2px',
  $4: '4px',
  $6: '6px',
  $8: '8px',
  $12: '12px',
  $16: '16px',
  $24: '24px',
  $32: '32px',
  $48: '48px',
  $64: '64px',
};

const EmotionThemeDark: EmotionTheme = {
  colors: EmotionThemeColorsDark,
  size: EmotionThemeSizes,
};

const EmotionThemeLight: EmotionTheme = {
  colors: EmotionThemeColorsLight,
  size: EmotionThemeSizes,
};

export {
  EmotionThemeDark,
  EmotionThemeLight,
};
