import classNames from 'classnames';
import React, { DetailedHTMLProps, HTMLAttributes, ReactNode } from 'react';
import styles from './Code.module.scss';

interface Props extends DetailedHTMLProps<HTMLAttributes<HTMLElement>, HTMLElement> {
  selectAll?: boolean;
  children: ReactNode;
}

/**
 * Use this to display code inline within some text.
 * For a multi-line block of code, use [CodeBlock] instead.
 */
const Code: React.FC<Props> = ({ className, selectAll = false, children, ...props }) => {
  return (
    <code className={classNames(styles.code, { [styles.selectAll]: selectAll }, className)} {...props}>
      {children}
    </code>
  );
};

export default Code;
