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
const Heading4: React.FC<Props> = ({ className = undefined, children }) => {
  return (
    <h4 className={classNames(styles.h4, className)}>
      {children}
    </h4>
  );
};

export default Heading4;
