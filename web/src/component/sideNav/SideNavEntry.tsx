import React from 'react';
import { NavLink } from 'react-router-dom';
import styles from './SideNavEntry.module.scss';

interface Props {
  label: string;
  to: string;
}

const SideNavEntry: React.FC<Props> = ({ label, to }) => {
  return (
    <li>
      <NavLink className={styles.link} to={to}>{label}</NavLink>
    </li>
  );
};

export default SideNavEntry;
