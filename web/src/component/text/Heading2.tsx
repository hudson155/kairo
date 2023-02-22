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
const Heading2: React.FC<Props> = ({ className = undefined, children }) => {
  return (
    <h2 className={classNames(styles.h2, className)}>
      {children}
    </h2>
  );
};

export default Heading2;
