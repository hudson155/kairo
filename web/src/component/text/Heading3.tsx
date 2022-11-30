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
const Heading3: React.FC<Props> = ({ className = undefined, children }) => {
  return (
    <h3 className={classNames(styles.h3, className)}>
      {children}
    </h3>
  );
};

export default Heading3;
