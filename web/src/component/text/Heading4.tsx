import classNames from 'classnames';
import React, { DetailedHTMLProps, HTMLAttributes, ReactNode } from 'react';
import styles from './Heading.module.scss';

interface Props extends DetailedHTMLProps<HTMLAttributes<HTMLHeadingElement>, HTMLHeadingElement> {
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
