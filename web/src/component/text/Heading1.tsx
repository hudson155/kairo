import classNames from 'classnames';
import styles from 'component/text/Heading.module.scss';
import React, { ReactNode } from 'react';

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
