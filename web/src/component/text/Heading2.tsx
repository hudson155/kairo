import classNames from 'classnames';
import React, { DetailedHTMLProps, HTMLAttributes, ReactNode } from 'react';
import styles from './Heading.module.scss';

interface Props extends DetailedHTMLProps<HTMLAttributes<HTMLHeadingElement>, HTMLHeadingElement> {
  children: ReactNode;
}

/**
 * Use semantic headings. Don't skip levels.
 */
const Heading2: React.FC<Props> = ({ className, children, ...props }) => {
  return (
    <h2 className={classNames(styles.h2, className)} {...props}>
      {children}
    </h2>
  );
};

export default Heading2;
