import Button from 'component/button/Button';
import Icon from 'component/icon/Icon';
import Menu from 'component/menu/Menu';
import MenuItem from 'component/menu/MenuItem';
import MenuItems from 'component/menu/MenuItems';
import ProfilePhoto from 'component/profilePhoto/ProfilePhoto';
import React from 'react';
import { useRecoilValueLoadable } from 'recoil';
import auth0ClientState from 'state/auth/auth0Client';
import styles from './TopNavMenu.module.scss';

const TopNavMenu: React.FC = () => {
  const button = (
    <Button variant="unstyled">
      <ProfilePhoto />
      <Icon className={styles.expandIcon} name="expand_more" />
    </Button>
  );

  return (
    <Menu button={button} side="right">
      <MenuItems>
        <MenuItem>
          {({ className }) => <LogoutButton className={className} />}
        </MenuItem>
      </MenuItems>
    </Menu>
  );
};

export default TopNavMenu;

const LogoutButton: React.FC<{ className: string }> = ({ className }) => {
  const auth0 = useRecoilValueLoadable(auth0ClientState).valueMaybe();
  if (!auth0) return null;
  return (
    <Button className={className} variant="unstyled" onClick={() => void auth0.logout()}>
      <Icon name="logout" size="small" space="after" />
      {`Log out`}
    </Button>
  );
};
