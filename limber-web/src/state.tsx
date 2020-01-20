import ThemeState from './redux/theme/ThemeState';
import UserState from './redux/user/UserState';
import OrgState from './redux/org/OrgState';
import AuthState from './redux/auth/AuthState';

export default interface State {
  auth: AuthState;
  org: OrgState;
  theme: ThemeState;
  user: UserState;
}
