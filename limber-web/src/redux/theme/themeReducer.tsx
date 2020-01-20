import ThemeState from './ThemeState';

const defaultState: ThemeState = {
  loadingStatus: 'LOADED',
  theme: {
    navBarColor: '#24292e',
    navBarLinkColor: 'white',
  },
};

const themeReducer = (state: ThemeState = defaultState): ThemeState => state;
export default themeReducer;
