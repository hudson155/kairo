import classNames from 'classnames';
import React, { HTMLAttributes } from 'react';
import styles from './Heading.module.scss';

type Props = HTMLAttributes<HTMLHeadingElement>

/**
 * Use semantic headings. Don't skip levels.
 */
const Heading1: React.FC<Props> =
  React.forwardRef<HTMLHeadingElement, Props>(({ className, children, ...props }, ref) => {
    return <h1 className={classNames(styles.h1, className)} ref={ref} {...props}>{children}</h1>;
  });

export default Heading1;
