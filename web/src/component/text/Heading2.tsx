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
const Heading2: React.FC<Props> = ({ className = undefined, children }) => {
  return (
    <h2 className={classNames(styles.h2, className)}>
      {children}
    </h2>
  );
};

export default Heading2;
