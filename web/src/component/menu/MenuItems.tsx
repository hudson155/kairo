import React, { PropsWithChildren, ReactNode } from 'react';
import styles from './MenuItems.module.scss';

interface Props extends PropsWithChildren {
  children: ReactNode;
}

const MenuItems: React.FC<Props> = ({ children }) => {
  return <div className={styles.items}>{children}</div>;
};

export default MenuItems;
