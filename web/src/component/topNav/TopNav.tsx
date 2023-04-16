import styles from 'component/topNav/TopNav.module.scss';
import React, { ReactNode } from 'react';

interface Props {
  left: ReactNode;
  right: ReactNode;
}

/**
 * This component styles the top navigation bar, but defines no functionality.
 * Functionality is defined in {@link TopNavImpl}.
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
