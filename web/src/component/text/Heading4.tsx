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
const Heading4: React.FC<Props> = ({ className = undefined, children }) => {
  return (
    <h4 className={classNames(styles.h4, className)}>
      {children}
    </h4>
  );
};

export default Heading4;
