import UserModel from '../../models/UserModel';
import { SetUserAction } from './UserAction';

function set(user: UserModel): SetUserAction {
  return { type: 'USER_SET', user };
}

const UserActions = {
  set,
};
export default UserActions;
