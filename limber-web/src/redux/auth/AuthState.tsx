import { LoadingStatus } from '../util/LoadingStatus';
import JwtUserModel from '../../models/auth/JwtUserModel';
import JwtOrgModel from '../../models/auth/JwtOrgModel';

export default interface AuthState {
  loadingStatus: LoadingStatus;
  jwt?: string;
  org?: JwtOrgModel;
  user?: JwtUserModel;
}
