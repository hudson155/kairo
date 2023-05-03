import { doNothing } from 'helper/doNothing';
import { useRef } from 'react';
import LocalStateContext, { LocalStateErrorContext, LocalStateLoadingContext, LocalStateValueContext } from 'state/local/LocalStateContext';

const LOCAL_STATE_CONTEXT_FEATURE_STATE_OPTIONS = ['Loading', 'Error', 'Empty', 'Normal'] as const;

export type LocalStateContextFixtureState = typeof LOCAL_STATE_CONTEXT_FEATURE_STATE_OPTIONS[number];

export const LOCAL_STATE_CONTEXT_FIXTURE_STATE_ARG_TYPE = {
  options: LOCAL_STATE_CONTEXT_FEATURE_STATE_OPTIONS,
  control: { type: 'radio' },
} as const;

export const useLocalStateContextFixture = <T>(
  state: LocalStateContextFixtureState,
  empty: T,
  normal: T,
): LocalStateContext<T> => {
  // These refs avoid unnecessary re-renders.
  const loadingRef = useRef(new LocalStateLoadingContext<T>(new Promise(doNothing)));
  const errorRef = useRef(new LocalStateErrorContext<T>(new Error('This is a sample local state context error.')));
  const emptyRef = useRef(new LocalStateValueContext<T>(empty));
  const normalRef = useRef(new LocalStateValueContext<T>(normal));

  switch (state) {
  case 'Loading':
    return loadingRef.current;
  case 'Error':
    return errorRef.current;
  case 'Empty':
    return emptyRef.current;
  case 'Normal':
    return normalRef.current;
  }
};
