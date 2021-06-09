import { useState } from 'react';

interface Result<T> {
  /**
   * Initially true, until loading is complete. When loading is complete, false regardless of
   * whether loading was successful or not.
   */
  readonly isLoading: boolean;

  /**
   * If loading was unsuccessful, throws the error that occurred. If loading was successful, returns
   * the loaded value.
   */
  get(): T
}

type State<T> = { isLoading: boolean, error?: Error, value?: T }

/**
 * This function is similar to React's built-in useState() hook, but allows for both result values
 * and errors to be stored.
 */
export default function useLoadingState<T>(): [Result<T>, (newValue: T) => void, (newError: Error) => void] {
  const [value, setValue] = useState<State<T>>({ isLoading: true });

  return [{
    isLoading: value.isLoading,
    get(): T {
      if (value.isLoading) throw new Error('Called getOrThrow() without an isLoading check.');
      if (value.error !== undefined) throw value.error;
      return value.value!;
    },
  }, value => {
    setValue({ isLoading: false, value });
  }, error => {
    setValue({ isLoading: false, error });
  }];
}
