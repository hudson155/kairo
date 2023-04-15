import Icon from 'component/icon/Icon';
import styles from 'component/sideNav/SideNavEntry.module.scss';
import React from 'react';
import { NavLink } from 'react-router-dom';

interface Props {
  iconName?: string;
  label: string;
  to: string;
  onClick?: () => void;
}

const SideNavEntry: React.FC<Props> = ({
  iconName = undefined,
  label,
  to,
  onClick = undefined,
}) => {
  return (
    <li>
      <NavLink className={styles.link} to={to} onClick={onClick}>
        {iconName ? <Icon key="icon" name={iconName} size="small" /> : null}
        {label}
      </NavLink>
    </li>
  );
};

export default SideNavEntry;
