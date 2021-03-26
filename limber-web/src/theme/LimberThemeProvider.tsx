import { ThemeProvider } from '@emotion/react';
import React, { useContext, useEffect } from 'react';
import usePersistentState from '../util/PersistentState';
import { EmotionThemeDark, EmotionThemeLight } from './EmotionTheme';

const LIMBER_THEME_KEY = 'LIMBER_THEME';

type LimberThemeType = 'DARK' | 'LIGHT';

interface LimberThemeContext {
  readonly themeType: LimberThemeType;
  readonly setThemeType: (themeType: LimberThemeType) => void;
}

const Context = React.createContext<LimberThemeContext>(undefined as unknown as LimberThemeContext);

const LimberThemeProvider: React.FC = ({ children }) => {
  const [themeType, setThemeType] = usePersistentState<LimberThemeType>(LIMBER_THEME_KEY, 'LIGHT');
  const theme = themeFromType(themeType);

  // General whole app theming. Should occur before any components are returned.
  // TODO: Finish the base default theming.
  useEffect(() => {
    document.body.style.backgroundColor = theme.colors.grey100;
    document.body.style.color = theme.colors.grey800;
  }, [themeType]);

  return (
    <Context.Provider value={{ themeType, setThemeType }}>
      <ThemeProvider theme={theme}>
        {children}
      </ThemeProvider>
    </Context.Provider>
  );
};

function themeFromType(themeType: LimberThemeType) {
  switch (themeType) {
    case 'DARK':
      return EmotionThemeDark;
    case 'LIGHT':
      return EmotionThemeLight;
    default:
      throw new Error(`Unknown theme type: ${themeType}.`);
  }
}

export default LimberThemeProvider;

export const useLimberTheme = (): LimberThemeContext => useContext(Context);
