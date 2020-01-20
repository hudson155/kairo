import UserModel from '../../models/user/UserModel';
import JwtUserModel from '../../models/user/JwtUserModel';

export default interface UserAction {
  type: 'USER__START_LOADING_USER' | 'USER__SET_JWT_USER' | 'USER__SET_USER';
}

export interface UserStartLoadingUserAction extends UserAction {
  type: 'USER__START_LOADING_USER';
}

export interface UserSetJwtUserAction extends UserAction {
  type: 'USER__SET_JWT_USER';
  jwtUser: JwtUserModel;
}

export interface UserSetUserAction extends UserAction {
  type: 'USER__SET_USER';
  user: UserModel;
}
