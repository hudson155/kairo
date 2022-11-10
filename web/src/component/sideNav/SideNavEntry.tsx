import React from 'react';
import { NavLink } from 'react-router-dom';
import styles from './SideNavEntry.module.scss';

interface Props {
  label: string;
  to: string;
  onClick?: () => void;
}

const SideNavEntry: React.FC<Props> = ({ label, onClick = undefined, to }) => {
  const handleClick = () => onClick?.();

  return (
    <li>
      <NavLink className={styles.link} to={to} onClick={handleClick}>{label}</NavLink>
    </li>
  );
};

export default SideNavEntry;
