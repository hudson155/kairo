import { LoadingStatus } from '../util/LoadingStatus';
import JwtUserModel from '../../models/auth/JwtUserModel';

export default interface AuthState {
  loadingStatus: LoadingStatus;
  jwt?: string;
  user?: JwtUserModel;
}
