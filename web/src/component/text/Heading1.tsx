import classNames from 'classnames';
import React, { HTMLAttributes } from 'react';
import styles from './Heading.module.scss';

type Props = HTMLAttributes<HTMLHeadingElement>

/**
 * Use semantic headings. Don't skip levels.
 */
const Heading1: React.FC<Props> = ({ className, children, ...props }) => {
  return (
    <h1 className={classNames(styles.h1, className)} {...props}>
      {children}
    </h1>
  );
};

export default Heading1;
