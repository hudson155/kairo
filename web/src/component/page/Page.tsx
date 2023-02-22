import React, { ReactNode } from 'react';
import styles from './Page.module.scss';

interface Props {
  children: ReactNode;
}

/**
 * This is normally the outermost component within a layout.
 * It separates sections vertically.
 */
const Page: React.FC<Props> = ({ children }) => {
  return (
    <div className={styles.page}>
      {children}
    </div>
  );
};

export default Page;
