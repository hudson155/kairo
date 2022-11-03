import classNames from 'classnames';
import React, { HTMLAttributes } from 'react';
import styles from './Text.module.scss';

export type TextSize = 'normal' | 'small'

interface Props extends HTMLAttributes<HTMLSpanElement> {
  /**
   * Don't provide this prop unless you need to override the text size.
   */
  size?: TextSize;
}

/**
 * Use this to adjust how text looks without adding additional spacing.
 * For document text that needs proper spacing, use [Paragraph] instead.
 */
const Text: React.FC<Props> =
  React.forwardRef<HTMLSpanElement, Props>(({ size, className, children, ...props }, ref) => {
    return (
      <span className={classNames(size ? styles[size] : undefined, className)}
            ref={ref}
            {...props}>
        {children}
      </span>
    );
  });

export default Text;
