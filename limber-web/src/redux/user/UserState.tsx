import UserModel from '../../models/user/UserModel';
import { LoadingStatus } from '../util/LoadingStatus';
import JwtUserModel from '../../models/user/JwtUserModel';

export default interface UserState {
  loadingStatus: LoadingStatus;
  jwtUser?: JwtUserModel;
  user?: UserModel;
}
