import classNames from 'classnames';
import React, { HTMLAttributes, ReactNode } from 'react';
import styles from './Code.module.scss';

interface Props extends HTMLAttributes<HTMLElement> {
  children: ReactNode;
}

/**
 * Use this to display code inline within some text.
 * For a multi-line block of code, use [CodeBlock] instead.
 */
const Code: React.FC<Props> = ({ className, children, ...props }) => {
  return (
    <code className={classNames(styles.code, className)} {...props}>
      {children}
    </code>
  );
};

export default Code;
