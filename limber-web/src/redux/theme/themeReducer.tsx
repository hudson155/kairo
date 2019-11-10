import ThemeAction from './ThemeAction';
import ThemeState from './ThemeState';

const defaultState: ThemeState = {
  loadingStatus: 'LOADED',
  theme: {
    navBarColor: '#24292e',
  },
};

const themeReducer = (state: ThemeState = defaultState, abstractAction: ThemeAction): ThemeState => {
  switch (abstractAction.type) {
    default:
      return state;
  }
};
export default themeReducer;
