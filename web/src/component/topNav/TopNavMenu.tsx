import Button from 'component/button/Button';
import Icon from 'component/icon/Icon';
import Menu from 'component/menu/Menu';
import MenuItem from 'component/menu/MenuItem';
import MenuItems from 'component/menu/MenuItems';
import ProfilePhoto from 'component/profilePhoto/ProfilePhoto';
import AdminSettingsButton from 'component/topNav/AdminSettingsButton';
import TopNavMenuLogoutButton from 'component/topNav/TopNavMenuLogoutButton';
import TopNavMenuSettingsButton from 'component/topNav/TopNavMenuSettingsButton';
import doNothing from 'helper/doNothing';
import React from 'react';
import styles from './TopNavMenu.module.scss';

const TopNavMenu: React.FC = () => {
  const button = (
    <Button variant="unstyled" onClick={doNothing}>
      <ProfilePhoto />
      <Icon className={styles.expandIcon} name="expand_more" />
    </Button>
  );

  return (
    <Menu button={button} side="right">
      <MenuItems>
        <MenuItem>
          {({ className }) => <TopNavMenuSettingsButton className={className} />}
        </MenuItem>
        <MenuItem>
          {({ className }) => <AdminSettingsButton className={className} />}
        </MenuItem>
      </MenuItems>
      <MenuItems>
        <MenuItem>
          {({ className }) => <TopNavMenuLogoutButton className={className} />}
        </MenuItem>
      </MenuItems>
    </Menu>
  );
};

export default TopNavMenu;
