import ThemeState from './redux/theme/ThemeState';
import UserState from './redux/user/UserState';

export default interface State {
  theme: ThemeState;
  user: UserState;
}
