import classNames from 'classnames';
import styles from 'component/banner/Banner.module.scss';
import React, { ReactNode } from 'react';

type Variant = 'success' | 'warning' | 'danger';

interface Props {
  variant: Variant;
  children: ReactNode;
}

/**
 * Banners are typically used to indicate whether an operation was successful or a state is desirable.
 */
const Banner: React.FC<Props> = ({ variant, children }) => {
  return (
    <div className={classNames(styles.banner, variantClassName(variant))}>
      {children}
    </div>
  );
};

export default Banner;

const variantClassName = (variant: Variant): string => {
  switch (variant) {
  case 'success':
    return styles.success;
  case 'warning':
    return styles.warning;
  case 'danger':
    return styles.danger;
  }
};
