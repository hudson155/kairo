import React, { DependencyList, useEffect } from 'react';

/**
 * Listens for events of the given {@param type}, calling {@param callback} for all events that
 * return true from {@param filter}. {@param deps} allows the filter to change. This hook is useful
 * for allowing components react to specific key or mouse events.
 */
export default function useFilteredEventListener<K extends keyof DocumentEventMap>(
  type: K,
  filter: (event: DocumentEventMap[K]) => boolean,
  callback: () => void,
  deps: DependencyList,
) {
  useEffect(() => {
    function eventListener(event: DocumentEventMap[K]) {
      if (filter(event)) {
        callback();
      }
    }

    document.addEventListener(type, eventListener);
    return () => {
      document.removeEventListener(type, eventListener);
    };
  }, deps);
}

/**
 * Listens for a click outside of the element with the given {@param ref}. Calls {@param callback}
 * for any such event.
 */
export function useOutsideClickListener(
  ref: React.MutableRefObject<any>,
  callback: () => void,
) {
  const filter = (event: MouseEvent) => ref.current && !ref.current.contains(event.target);
  useFilteredEventListener('click', filter, callback, [ref]);
}

/**
 * Listens for the escape key to be pressed. Calls {@param callback} for any such event.
 */
export function useEscapeKeyListener(callback: () => void) {
  const filter = (event: KeyboardEvent) => event.key === 'Escape';
  useFilteredEventListener('keydown', filter, callback, []);
}
