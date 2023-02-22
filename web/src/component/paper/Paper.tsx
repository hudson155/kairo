import classNames from 'classnames';
import React, { ReactNode } from 'react';
import styles from './Paper.module.scss';

interface Props {
  children: ReactNode;
}

/**
 * A container component that somewhat resembles a piece of paper.
 * Okay, not really. But what else are you gonna name it?
 */
const Paper: React.FC<Props> = ({ children }) => {
  return (
    <div className={classNames(styles.paper)}>
      {children}
    </div>
  );
};

export default Paper;
