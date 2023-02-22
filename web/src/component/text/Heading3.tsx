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
const Heading3: React.FC<Props> = ({ className = undefined, children }) => {
  return (
    <h3 className={classNames(styles.h3, className)}>
      {children}
    </h3>
  );
};

export default Heading3;
