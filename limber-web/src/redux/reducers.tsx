import { combineReducers } from 'redux';
import authReducer from './auth/authReducer';
import orgReducer from './org/orgReducer';
import tenantReducer from './tenant/tenantReducer';
import themeReducer from './theme/themeReducer';
import userReducer from './user/userReducer';

export default combineReducers({
  auth: authReducer,
  org: orgReducer,
  tenant: tenantReducer,
  theme: themeReducer,
  user: userReducer,
});
