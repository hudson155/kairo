import { combineReducers } from 'redux';
import themeReducer from './theme/themeReducer';
import userReducer from './user/userReducer';

export default combineReducers({
  theme: themeReducer,
  user: userReducer,
});
