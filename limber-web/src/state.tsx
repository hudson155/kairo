import AuthState from './redux/auth/AuthState';
import OrgState from './redux/org/OrgState';
import TenantState from './redux/tenant/TenantState';
import ThemeState from './redux/theme/ThemeState';
import UserState from './redux/user/UserState';

export default interface State {
  auth: AuthState;
  org: OrgState;
  tenant: TenantState;
  theme: ThemeState;
  user: UserState;
}
