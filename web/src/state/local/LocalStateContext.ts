import React, { DependencyList, useEffect, useState } from 'react';

export class LocalStateValueContext<T> {
  readonly state = 'hasValue';

  readonly contents: T;

  constructor(contents: T) {
    this.contents = contents;
  }

  getValue(): T {
    return this.contents;
  }
}

export class LocalStateErrorContext<T> {
  readonly state = 'hasError';

  readonly contents: unknown;

  constructor(contents: unknown) {
    this.contents = contents;
  }

  getValue(): T {
    throw this.contents;
  }
}

export class LocalStateLoadingContext<T> {
  readonly state = 'loading';

  readonly contents: Promise<T>;

  constructor(contents: Promise<T>) {
    this.contents = contents;
  }

  /**
   * The loading implementation throws a promise.
   * Although that's generally not allowed by the linter,
   * it's the behaviour that React Suspense expects.
   */
  getValue(): T {
    // eslint-disable-next-line @typescript-eslint/no-throw-literal
    throw this.contents;
  }
}

/**
 * Local state context allows state to be DOM-local instead of global.
 * This approach is preferred for most state because it avoids memory hogging and freshness issues with global state,
 * but still allows render-as-you-fetch semantics using Suspense and ErrorBoundary, similar to Recoil.
 *
 * This is a type union instead of a base class to allow TypeScript to auto-cast based on the value of {@link state}.
 */
type LocalStateContext<T> = LocalStateValueContext<T> | LocalStateErrorContext<T> | LocalStateLoadingContext<T>;

export default LocalStateContext;

export const createLocalStateContext = <T>(): React.Context<LocalStateContext<T>> => {
  return React.createContext<LocalStateContext<T>>(
    undefined as unknown as LocalStateContext<T>,
  );
};

export const useLocalStateContext = <T>(
  block: () => Promise<T>,
  deps: DependencyList,
): LocalStateContext<T> | undefined => {
  const [contents, setContents] = useState<LocalStateContext<T>>();

  useEffect(() => {
    const promise = block();
    void (async () => {
      let result: LocalStateContext<T>;
      try {
        result = new LocalStateValueContext<T>(await promise);
      } catch (e) {
        result = new LocalStateErrorContext<T>(e);
      }
      setContents(result);
      return result;
    })();
    setContents(new LocalStateLoadingContext<T>(promise));
  }, deps); // eslint-disable-line react-hooks/exhaustive-deps

  return contents;
};
