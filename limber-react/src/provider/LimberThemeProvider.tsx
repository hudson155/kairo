import { ThemeProvider } from 'emotion-theming';
import React, { ReactElement, ReactNode, useContext, useEffect } from 'react';

import { EmotionThemeDark, EmotionThemeLight } from '../app/EmotionTheme';
import usePersistentState from '../app/utils/usePersistentState';

const LIMBER_THEME_KEY = 'LIMBER_IS_LIGHT_THEME_KEY';

interface Props {
  readonly children: ReactNode;
}

interface LimberThemeContext {
  readonly isLightTheme: boolean;
  readonly setIsLightTheme: (isLightTheme: boolean) => void;
}

const Context = React.createContext<LimberThemeContext>({
  isLightTheme: true,
  // eslint-disable-next-line @typescript-eslint/no-empty-function
  setIsLightTheme: () => {},
});

function LimberThemeProvider(props: Props): ReactElement {
  const [isLightTheme, setIsLightTheme] = usePersistentState(LIMBER_THEME_KEY, true);
  const theme = isLightTheme ? EmotionThemeLight : EmotionThemeDark;

  // General whole app theming. Should occur before any components are returned.
  // TODO (ENG-82): Complete the base default theming
  useEffect(() => {
    document.body.style.backgroundColor = theme.colors.grey100;
    document.body.style.color = theme.colors.grey800;
  }, [theme]);

  return (
    <Context.Provider value={{ isLightTheme, setIsLightTheme }}>
      <ThemeProvider theme={theme}>
        {props.children}
      </ThemeProvider>
    </Context.Provider>
  );
}

export const useLimberTheme = (): LimberThemeContext => useContext(Context);
export default LimberThemeProvider;
