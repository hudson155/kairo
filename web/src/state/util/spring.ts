import { atom, DefaultValue, GetCallback, GetRecoilValue, Loadable, RecoilState, RecoilValue, selector, WrappedValue } from 'recoil';

interface SpringOptions<T> {
  key: string;
  get: (opts: {
    get: GetRecoilValue,
    getCallback: GetCallback,
  }) => Promise<T> | RecoilValue<T> | Loadable<T> | WrappedValue<T> | T;
  dangerouslyAllowMutability?: boolean;
}

/**
 * A [spring] is a combination of a [selector] and an [atom].
 * There are 2 differences from a [selector].
 *   1. The value can be set using [useSetRecoilState].
 *   2. The value can be reset using [useResetRecoilState].
 */
function spring<T>(options: SpringOptions<T>): RecoilState<T> { // eslint-disable-line func-style
  /**
   * The value of [refreshState] can be incremented to force-refresh the spring upon reset.
   */
  const refreshState = atom<number>({
    key: `${options.key}-refresh`,
    default: 0,
  });

  /**
   * The value of [localState] can be set to override the spring's selector value.
   */
  const localState = atom<T>({
    key: `${options.key}-atom`,
    default: undefined,
  });

  return selector<T>({
    key: `${options.key}-selector`,
    get: (opts) => {
      // If there's a local value set, we use it.
      const localValue = opts.get(localState);
      if (localValue) return localValue;

      opts.get(refreshState); // Enables force-refreshing upon reset.
      return options.get(opts);
    },
    set: (opts, newValue) => {
      if (newValue instanceof DefaultValue) {
        opts.set(refreshState, opts.get(refreshState) + 1);
        opts.reset(localState);
      } else {
        opts.set(localState, newValue);
      }
    },
    dangerouslyAllowMutability: options.dangerouslyAllowMutability ?? false,
    // Caching is not useful here.
    cachePolicy_UNSTABLE: { eviction: 'most-recent' }, // eslint-disable-line @typescript-eslint/naming-convention
  });
}

export default spring;
