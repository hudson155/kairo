import UserModel from '../../models/user/UserModel';
import { SetJwtUserAction, SetUserAction } from './UserAction';
import JwtUserModel from '../../models/user/JwtUserModel';

function setJwtUser(jwtUser: JwtUserModel): SetJwtUserAction {
  return { type: 'USER__SET_JWT_USER', jwtUser };
}

function setUser(user: UserModel): SetUserAction {
  return { type: 'USER__SET_USER', user };
}

const UserActions = {
  setJwtUser,
  setUser,
};
export default UserActions;
