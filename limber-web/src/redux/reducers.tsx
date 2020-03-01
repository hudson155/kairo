import { combineReducers } from 'redux';
import themeReducer from './theme/themeReducer';
import userReducer from './user/userReducer';
import authReducer from './auth/authReducer';
import orgReducer from './org/orgReducer';
import tenantReducer from './tenant/tenantReducer';

export default combineReducers({
  auth: authReducer,
  org: orgReducer,
  tenant: tenantReducer,
  theme: themeReducer,
  user: userReducer,
});
