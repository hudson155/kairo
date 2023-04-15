import Button from 'component/button/Button';
import Icon from 'component/icon/Icon';
import Menu from 'component/menu/Menu';
import MenuItem from 'component/menu/MenuItem';
import MenuItems from 'component/menu/MenuItems';
import ProfilePhoto from 'component/profilePhoto/ProfilePhoto';
import AdminSettingsButton from 'component/topNav/AdminSettingsButton';
import OrganizationSettingsButton from 'component/topNav/OrganizationSettingsButton';
import styles from 'component/topNav/TopNavMenu.module.scss';
import TopNavMenuLogoutButton from 'component/topNav/TopNavMenuLogoutButton';
import { doNothing } from 'helper/doNothing';
import React from 'react';

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
          <OrganizationSettingsButton />
        </MenuItem>
        <MenuItem>
          <AdminSettingsButton />
        </MenuItem>
      </MenuItems>
      <MenuItems>
        <MenuItem>
          <TopNavMenuLogoutButton />
        </MenuItem>
      </MenuItems>
    </Menu>
  );
};

export default TopNavMenu;
