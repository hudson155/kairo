import Icon from 'component/icon/Icon';
import { ADMIN_SETTINGS_PAGE_PATH } from 'page/adminSettings/AdminSettingsPageRoute';
import React from 'react';
import { Link } from 'react-router-dom';

interface Props {
  className: string;
  onClick: () => void;
}

const AdminSettingsButton: React.ForwardRefRenderFunction<HTMLAnchorElement, Props> =
  ({ className, onClick }, ref) => {
    return (
      <Link ref={ref} className={className} to={ADMIN_SETTINGS_PAGE_PATH} onClick={onClick}>
        <Icon name="admin_panel_settings" size="small" space="after" />
        {'Admin settings'}
      </Link>
    );
  };

export default React.forwardRef(AdminSettingsButton);
