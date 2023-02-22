import classNames from 'classnames';
import React, { ReactNode, useEffect, useRef, useState } from 'react';
import { durationCssDelay, durationTransition } from 'style/theme/duration';
import styles from './Collapsible.module.scss';

interface Props {
  className?: string;
  isOpen: boolean;
  children: ReactNode;
}

/**
 * Vertical transitions are not possible in CSS.
 * This component enables vertical transitions without introducing too much latency.
 * It does this not by continually updating the vertical size,
 * but by letting CSS perform the transitions based on a JS-computed value.
 */
const Collapsible: React.FC<Props> = ({ className = undefined, isOpen, children }) => {
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
      }, durationCssDelay);
      setContentTimeout(setTimeout(() => {
        element.style.height = 'auto';
      }, durationCssDelay + durationTransition));
    } else {
      element.style.height = `${element.scrollHeight}px`;
      setTimeout(() => {
        element.style.height = '0';
      }, durationCssDelay);
      setContentTimeout(setTimeout(() => {
        setContent(null);
      }, durationCssDelay + durationTransition));
    }
    clearTimeout(contentTimeout);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isOpen]);

  return <div ref={ref} className={classNames(styles.container, className)}>{content}</div>;
};

export default Collapsible;
