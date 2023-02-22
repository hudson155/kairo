import styles from 'component/menu/MenuItems.module.scss';
import React, { ReactNode } from 'react';

interface Props {
  children: ReactNode;
}

const MenuItems: React.FC<Props> = ({ children }) => {
  return <div className={styles.items}>{children}</div>;
};

export default MenuItems;
