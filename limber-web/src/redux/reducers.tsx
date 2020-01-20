import { combineReducers } from 'redux';
import themeReducer from './theme/themeReducer';
import userReducer from './user/userReducer';
import authReducer from './auth/authReducer';
import orgReducer from './org/orgReducer';

export default combineReducers({
  auth: authReducer,
  org: orgReducer,
  theme: themeReducer,
  user: userReducer,
});
