import UserModel from '../../models/user/UserModel';

export default interface UserAction {
  type: 'USER__SET';
}

export interface SetUserAction extends UserAction {
  type: 'USER__SET';
  user: UserModel;
}
