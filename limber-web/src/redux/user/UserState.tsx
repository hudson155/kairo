import UserModel from '../../models/user/UserModel';
import { LoadingStatus } from '../util/LoadingStatus';

export default interface UserState {
  loadingStatus: LoadingStatus;
  user?: UserModel;
}
