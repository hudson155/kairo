import { LoadingStatus } from '../util/LoadingStatus';

export default interface AuthState {
  loadingStatus: LoadingStatus;
  jwt?: string;
}
