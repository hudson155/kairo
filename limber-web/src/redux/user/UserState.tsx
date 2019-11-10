import UserModel from '../../models/UserModel';

export default interface UserState {
  loadingStatus: LoadingStatus;
  user?: UserModel;
}
