import UserModel from '../../models/user/UserModel';

export default interface UserAction {
  type: 'USER__START_LOADING_USER' | 'USER__SET_USER';
}

export interface UserStartLoadingUserAction extends UserAction {
  type: 'USER__START_LOADING_USER';
}

export interface UserSetUserAction extends UserAction {
  type: 'USER__SET_USER';
  user: UserModel;
}
