import Button from 'component/button/Button';
import Icon from 'component/icon/Icon';
import Menu from 'component/menu/Menu';
import MenuItem from 'component/menu/MenuItem';
import MenuItems from 'component/menu/MenuItems';
import ProfilePhoto from 'component/profilePhoto/ProfilePhoto';
import TopNavMenuLogoutButton from 'component/topNav/TopNavMenuLogoutButton';
import React from 'react';
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
          {({ className }) => <TopNavMenuLogoutButton className={className} />}
        </MenuItem>
      </MenuItems>
    </Menu>
  );
};

export default TopNavMenu;
