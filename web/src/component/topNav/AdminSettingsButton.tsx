import Icon from 'component/icon/Icon';
import adminSettingsRoute from 'page/adminSettings/adminSettingsRoute';
import React from 'react';
import { Link, LinkProps } from 'react-router-dom';

type Props = Omit<LinkProps, 'to' | 'children'>;

const AdminSettingsButton: React.ForwardRefRenderFunction<HTMLAnchorElement, Props> =
  ({ ...props }, ref) => {
    return (
      <Link ref={ref} to={adminSettingsRoute.path} {...props}>
        <Icon name="admin_panel_settings" size="small" />
        {'Admin settings'}
      </Link>
    );
  };

export default React.forwardRef(AdminSettingsButton);
