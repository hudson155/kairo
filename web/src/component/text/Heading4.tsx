import classNames from 'classnames';
import React, { HTMLAttributes } from 'react';
import styles from './Heading.module.scss';

type Props = HTMLAttributes<HTMLHeadingElement>

/**
 * Use semantic headings. Don't skip levels.
 */
const Heading4: React.FC<Props> =
  React.forwardRef<HTMLHeadingElement, Props>(({ className, children, ...props }, ref) => {
    return <h4 className={classNames(styles.h4, className)} ref={ref} {...props}>{children}</h4>;
  });

export default Heading4;
