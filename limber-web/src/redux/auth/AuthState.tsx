import AuthModel from '../../models/auth/AuthModel';
import { LoadingStatus } from '../util/LoadingStatus';

export default interface AuthState {
  loadingStatus: LoadingStatus;
  auth?: AuthModel
}
