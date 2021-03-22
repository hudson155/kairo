import { useEffect, useState } from 'react';

// Borrowed from: https://joshwcomeau.com/react/persisting-react-state-in-localstorage/

function getPersistentState<T>(key: string, defaultValue: T): T {
  const persistentValue = window.localStorage.getItem(key);
  return persistentValue != null
    ? JSON.parse(persistentValue)
    : defaultValue;
}

function usePersistentState<T>(key: string, defaultValue: T): [T, (newValue: T) => void] {
  const [value, setValue] = useState(() => getPersistentState(key, defaultValue));

  // If the key change, let's re-fetch the value
  useEffect(() => {
    const newValue = getPersistentState(key, defaultValue);
    setValue(newValue);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [key]);

  // If the value is set, we want to save it
  useEffect(() => {
    window.localStorage.setItem(key, JSON.stringify(value));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [value]);

  return [value, setValue];
}

export default usePersistentState;
