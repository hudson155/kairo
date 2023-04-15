import Icon from 'component/icon/Icon';
import { ORGANIZATION_SETTINGS_PAGE_PATH } from 'page/organizationSettings/OrganizationSettingsPageRoute';
import React from 'react';
import { Link, LinkProps } from 'react-router-dom';

type Props = Omit<LinkProps, 'to' | 'children'>;

const OrganizationSettingsButton: React.ForwardRefRenderFunction<HTMLAnchorElement, Props> =
  ({ ...props }, ref) => {
    return (
      <Link ref={ref} to={ORGANIZATION_SETTINGS_PAGE_PATH} {...props}>
        <Icon name="settings" size="small" />
        {'Organization settings'}
      </Link>
    );
  };

export default React.forwardRef(OrganizationSettingsButton);
