import UserModel from '../../models/user/UserModel';
import { SetUserAction } from './UserAction';

function set(user: UserModel): SetUserAction {
  return { type: 'USER__SET', user };
}

const UserActions = {
  set,
};
export default UserActions;
