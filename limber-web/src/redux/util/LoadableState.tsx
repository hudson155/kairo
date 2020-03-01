import { LoadingStatus } from './LoadingStatus';

export default interface LoadableState<T> {
  loadingStatus: LoadingStatus;
  model?: T;
  errorMessage?: string;
}

export function assertLoaded<T>(loadableState: LoadableState<T>): T {
  if (loadableState.loadingStatus !== 'LOADED') {
    throw Error(`Expected ${loadableState.constructor.name} to be loaded but it was not.`);
  }
  return loadableState.model!;
}
