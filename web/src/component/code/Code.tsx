import classNames from 'classnames';
import React, { ReactNode } from 'react';
import styles from './Code.module.scss';

interface Props {
  className?: string;
  selectAll?: boolean;
  children: ReactNode;
}

/**
 * Use this to display code inline within some text.
 * For a multi-line block of code, use [CodeBlock] instead.
 */
const Code: React.FC<Props> = ({ className = undefined, selectAll = false, children }) => {
  return (
    <code className={classNames(styles.code, { [styles.selectAll]: selectAll }, className)}>
      {children}
    </code>
  );
};

export default Code;
