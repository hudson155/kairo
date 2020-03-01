import ThemeState from './redux/theme/ThemeState';
import UserState from './redux/user/UserState';
import OrgState from './redux/org/OrgState';
import AuthState from './redux/auth/AuthState';
import TenantState from './redux/tenant/TenantState';

export default interface State {
  auth: AuthState;
  org: OrgState;
  tenant: TenantState;
  theme: ThemeState;
  user: UserState;
}
