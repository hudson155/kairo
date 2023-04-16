import Icon from 'component/icon/Icon';
import organizationSettingsRoute from 'page/organizationSettings/organizationSettingsRoute';
import React from 'react';
import { Link, LinkProps } from 'react-router-dom';

type Props = Omit<LinkProps, 'to' | 'children'>;

const OrganizationSettingsButton: React.ForwardRefRenderFunction<HTMLAnchorElement, Props> =
  ({ ...props }, ref) => {
    return (
      <Link ref={ref} to={organizationSettingsRoute.path} {...props}>
        <Icon name="settings" size="small" />
        {'Organization settings'}
      </Link>
    );
  };

export default React.forwardRef(OrganizationSettingsButton);
