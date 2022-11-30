import classNames from 'classnames';
import React, { ReactNode } from 'react';
import styles from './Heading.module.scss';

interface Props {
  className?: string;
  children: ReactNode;
}

/**
 * Use semantic headings. Don't skip levels.
 */
const Heading1: React.FC<Props> = ({ className = undefined, children }) => {
  return (
    <h1 className={classNames(styles.h1, className)}>
      {children}
    </h1>
  );
};

export default Heading1;
