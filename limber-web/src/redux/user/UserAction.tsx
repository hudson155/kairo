import UserModel from '../../models/UserModel';

export default interface UserAction {
  type: 'USER_SET';
}

export interface SetUserAction extends UserAction {
  type: 'USER_SET';
  user: UserModel;
}
