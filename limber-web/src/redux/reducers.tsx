import { combineReducers } from 'redux';
import themeReducer from './theme/themeReducer';

export default combineReducers({
  theme: themeReducer,
});
