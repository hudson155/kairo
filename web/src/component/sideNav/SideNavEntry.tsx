import Icon from 'component/icon/Icon';
import React, { ReactNode } from 'react';
import { NavLink } from 'react-router-dom';
import styles from './SideNavEntry.module.scss';

interface Props {
  iconName?: string;
  label: string;
  to: string;
  onClick: () => void;
}

const SideNavEntry: React.FC<Props> = ({
  iconName = undefined,
  label,
  onClick = undefined,
  to,
}) => {
  return (
    <li>
      <NavLink className={styles.link} to={to} onClick={onClick}>{children(iconName, label)}</NavLink>
    </li>
  );
};

export default SideNavEntry;

const children = (iconName: string | undefined, label: string): ReactNode => {
  const result: ReactNode[] = [];
  if (iconName) result.push(<Icon key="icon" name={iconName} size="small" space="after" />);
  result.push(label);
  return <>{result}</>;
};
