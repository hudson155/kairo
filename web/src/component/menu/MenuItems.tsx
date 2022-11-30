import React, { ReactNode } from 'react';
import styles from './MenuItems.module.scss';

interface Props {
  children: ReactNode;
}

const MenuItems: React.FC<Props> = ({ children }) => {
  return <div className={styles.items}>{children}</div>;
};

export default MenuItems;
