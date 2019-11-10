import UserModel from '../../models/UserModel';
import LoadingStatus from '../util/LoadingStatus';

export default interface UserState {
  loadingStatus: LoadingStatus;
  user?: UserModel;
}
