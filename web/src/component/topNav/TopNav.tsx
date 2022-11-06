import React, { ReactNode } from 'react';
import styles from './TopNav.module.scss';

interface Props {
  left: ReactNode;
  right: ReactNode;
}

/**
 * This component styles the top navigation bar, but defines no functionality.
 * Functionality is defined in [TopNavImpl].
 */
const TopNav: React.FC<Props> = ({ left, right }) => {
  return (
    <header className={styles.header}>
      <div className={styles.left}>{left}</div>
      <div className={styles.right}>{right}</div>
    </header>
  );
};

export default TopNav;
