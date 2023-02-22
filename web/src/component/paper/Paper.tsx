import classNames from 'classnames';
import styles from 'component/paper/Paper.module.scss';
import React, { ReactNode } from 'react';

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
