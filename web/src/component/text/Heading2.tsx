import classNames from 'classnames';
import React, { HTMLAttributes } from 'react';
import styles from './Heading.module.scss';

type Props = HTMLAttributes<HTMLHeadingElement>

/**
 * Use semantic headings. Don't skip levels.
 */
const Heading2: React.FC<Props> =
  React.forwardRef<HTMLHeadingElement, Props>(({ className, children, ...props }, ref) => {
    return <h2 className={classNames(styles.h2, className)} ref={ref} {...props}>{children}</h2>;
  });

export default Heading2;
