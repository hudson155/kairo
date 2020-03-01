import AuthModel from './models/auth/AuthModel';
import OrgModel from './models/org/OrgModel';
import TenantModel from './models/tenant/TenantModel';
import ThemeModel from './models/theme/ThemeModel';
import UserModel from './models/user/UserModel';
import LoadableState from './redux/util/LoadableState';

export default interface State {
  auth: LoadableState<AuthModel>;
  org: LoadableState<OrgModel>;
  tenant: LoadableState<TenantModel>;
  theme: LoadableState<ThemeModel>;
  user: LoadableState<UserModel>;
}
