import classNames from 'classnames';
import React, { PropsWithChildren, ReactNode } from 'react';
import styles from './Paper.module.scss';

interface Props extends PropsWithChildren {
  children: ReactNode;
}

/**
 * A container component that somewhat resembles a piece of paper.
 * Okay, not really. But what else are you gonna name it?
 */
const Paper: React.FC<Props> = ({ children, ...props }) => {
  return (
    <div className={classNames(styles.container)} {...props}>
      {children}
    </div>
  );
};

export default Paper;
