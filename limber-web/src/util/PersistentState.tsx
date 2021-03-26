import { useEffect, useState } from 'react';

/**
 * Borrowed from https://joshwcomeau.com/react/persisting-react-state-in-localstorage/, this
 * function is similar to React's built-in useState() hook, but stores the value in the browser's
 * local storage for persistence between sessions.
 */
export default function usePersistentState<T>(key: string, defaultValue: T): [T, (newValue: T) => void] {
  const [value, setValue] = useState(() => getPersistentState(key, defaultValue));

  // If the key change, let's re-fetch the value.
  useEffect(() => {
    setValue(getPersistentState(key, defaultValue));
  }, [key]);

  // If the value is set, we want to save it.
  useEffect(() => {
    window.localStorage.setItem(key, JSON.stringify(value));
  }, [value]);

  return [value, setValue];
}

function getPersistentState<T>(key: string, defaultValue: T): T {
  const persistentValue = window.localStorage.getItem(key);
  return persistentValue !== null ? JSON.parse(persistentValue) : defaultValue;
}
