import { LoadingStatus } from './LoadingStatus';

export default interface LoadableState<T> {
  loadingStatus: LoadingStatus;
  model?: T
}
