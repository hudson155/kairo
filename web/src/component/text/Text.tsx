import classNames from 'classnames';
import React, { HTMLAttributes, ReactNode } from 'react';
import styles from './Text.module.scss';

export type TextSize = 'normal' | 'small';

interface Props extends HTMLAttributes<HTMLSpanElement> {
  /**
   * Don't provide this prop unless you need to override the text size.
   */
  size?: TextSize;

  children: ReactNode;
}

/**
 * Use this to adjust how text looks without adding additional spacing.
 * For document text that needs proper spacing, use [Paragraph] instead.
 */
const Text: React.FC<Props> = ({ className, size = undefined, children, ...props }) => {
  return (
    <span className={classNames(size ? styles[size] : undefined, className)} {...props}>
      {children}
    </span>
  );
};

export default Text;
