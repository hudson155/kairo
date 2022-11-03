import classNames from 'classnames';
import React, { HTMLAttributes } from 'react';
import styles from './Heading.module.scss';

type Props = HTMLAttributes<HTMLHeadingElement>

/**
 * Use semantic headings. Don't skip levels.
 */
const Heading3: React.FC<Props> =
  React.forwardRef<HTMLHeadingElement, Props>(({ className, children, ...props }, ref) => {
    return <h3 className={classNames(styles.h3, className)} ref={ref} {...props}>{children}</h3>;
  });

export default Heading3;
