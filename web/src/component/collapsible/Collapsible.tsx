import React, { ReactNode, useEffect, useRef, useState } from 'react';
import { durationTransition } from 'style/theme';
import styles from './Collapsible.module.scss';

interface Props {
  isOpen: boolean;
  children: ReactNode;
}

/**
 * Vertical transitions are not possible in CSS.
 * This component enables vertical transitions without introducing too much latency.
 * It does this not by continually updating the vertical size,
 * but by letting CSS perform the transitions based on a JS-computed value.
 */
const Collapsible: React.FC<Props> = ({ isOpen, children }) => {
  const ref = useRef<HTMLDivElement>(null);

  const [content, setContent] = useState<ReactNode>(isOpen ? children : null);
  const [contentTimeout, setContentTimeout] = useState<ReturnType<typeof setTimeout>>();

  useEffect(() => {
    const element = ref.current;
    if (!element) return;
    if (isOpen) {
      setContent(children);
      setTimeout(() => {
        element.style.height = `${element.scrollHeight}px`;
      }, 0);
    } else {
      element.style.height = `${element.scrollHeight}px`;
      setTimeout(() => {
        element.style.height = '0';
      }, 0);
      setContentTimeout(setTimeout(() => {
        setContent(null);
      }, durationTransition));
    }
    clearTimeout(contentTimeout);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isOpen]);

  return <div ref={ref} className={styles.container}>{content}</div>;
};

export default Collapsible;
