import ThemeAction, { SetThemeAction } from './ThemeAction';
import ThemeState from './ThemeState';

const defaultState: ThemeState = {
  navBarColor: '#24292e',
};

const themeReducer = (state: ThemeState = defaultState, abstractAction: ThemeAction): ThemeState => {
  switch (abstractAction.type) {
    case 'THEME_SET': {
      const action = abstractAction as SetThemeAction;
      return action.theme;
    }
    default:
      return state;
  }
};
export default themeReducer;
