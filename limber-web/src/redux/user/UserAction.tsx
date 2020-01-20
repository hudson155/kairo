import UserModel from '../../models/user/UserModel';
import JwtUserModel from '../../models/user/JwtUserModel';

export default interface UserAction {
  type: 'USER__SET_JWT_USER' | 'USER__SET_USER';
}

export interface SetJwtUserAction extends UserAction {
  type: 'USER__SET_JWT_USER';
  jwtUser: JwtUserModel;
}

export interface SetUserAction extends UserAction {
  type: 'USER__SET_USER';
  user: UserModel;
}
