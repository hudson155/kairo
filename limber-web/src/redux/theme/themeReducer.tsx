import ThemeModel from '../../models/theme/ThemeModel';
import LoadableState from '../util/LoadableState';

const defaultState: LoadableState<ThemeModel> = {
  loadingStatus: 'LOADED',
  model: {
    navBarColor: '#24292e',
    navBarLinkColor: 'white',
  },
};

const themeReducer = (state: LoadableState<ThemeModel> = defaultState): LoadableState<ThemeModel> => state;

export default themeReducer;
