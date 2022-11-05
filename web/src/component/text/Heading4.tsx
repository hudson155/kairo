import classNames from 'classnames';
import React, { HTMLAttributes, ReactNode } from 'react';
import styles from './Heading.module.scss';

interface Props extends HTMLAttributes<HTMLHeadingElement> {
  children: ReactNode;
}

/**
 * Use semantic headings. Don't skip levels.
 */
const Heading4: React.FC<Props> = ({ className, children, ...props }) => {
  return (
    <h4 className={classNames(styles.h4, className)} {...props}>
      {children}
    </h4>
  );
};

export default Heading4;
