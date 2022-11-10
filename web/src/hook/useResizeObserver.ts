/* eslint-disable compat/compat */

import { useEffect, useRef, useState } from 'react';

/**
 * This hook uses the observer pattern to listen for changes to the browser "client width".
 * It's more performant than using listeners.
 * See https://developer.mozilla.org/en-US/docs/Web/API/Resize_Observer_API.
 *
 * USE THIS SPARINGLY. Most of the time we should prefer CSS.
 */
export const useResizeObserver = (matcher: (size: number) => boolean): boolean => {
  const [size, setSize] = useState(width(document.documentElement));

  const observer = useRef(new ResizeObserver(([entry]) => setSize(width(entry.target))));

  useEffect(() => {
    observer.current.observe(document.documentElement);
    // eslint-disable-next-line react-hooks/exhaustive-deps
    return () => observer.current.unobserve(document.documentElement);
  }, []);

  return matcher(size);
};

const width = (element: Element): number => element.clientWidth;
