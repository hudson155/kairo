import classNames from 'classnames';
import styles from 'component/paper/Paper.module.scss';
import React, { ReactNode } from 'react';

type Variant = 'default' | 'danger';

interface Props {
  variant?: Variant;
  children: ReactNode;
}

/**
 * A container component that somewhat resembles a piece of paper.
 * Okay, not really. But what else are you gonna name it?
 */
const Paper: React.FC<Props> = ({ variant = 'default', children }) => {
  return (
    <div className={classNames(styles.paper, variantClassName(variant))}>
      {children}
    </div>
  );
};

export default Paper;

const variantClassName = (variant: Variant): string => {
  switch (variant) {
  case 'default':
    return styles.default;
  case 'danger':
    return styles.danger;
  }
};
