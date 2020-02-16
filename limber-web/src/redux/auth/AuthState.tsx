import { LoadingStatus } from '../util/LoadingStatus';
import AuthModel from '../../models/auth/AuthModel';

export default interface AuthState {
  loadingStatus: LoadingStatus;
  auth?: AuthModel
}
