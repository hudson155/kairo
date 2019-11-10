import ThemeState from './redux/theme/ThemeState';
import UserState from './redux/user/UserState';
import OrgsState from './redux/orgs/OrgsState';
import AuthState from './redux/auth/AuthState';

export default interface State {
  auth: AuthState;
  orgs: OrgsState;
  theme: ThemeState;
  user: UserState;
}
