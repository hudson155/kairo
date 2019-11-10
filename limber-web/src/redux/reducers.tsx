import { combineReducers } from 'redux';
import themeReducer from './theme/themeReducer';
import userReducer from './user/userReducer';
import authReducer from './auth/authReducer';
import orgsReducer from './orgs/orgsReducer';

export default combineReducers({
  auth: authReducer,
  orgs: orgsReducer,
  theme: themeReducer,
  user: userReducer,
});
