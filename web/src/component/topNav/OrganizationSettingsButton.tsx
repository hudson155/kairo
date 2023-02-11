import Icon from 'component/icon/Icon';
import { ORGANIZATION_SETTINGS_PAGE_PATH } from 'page/organizationSettings/OrganizationSettingsPageRoute';
import React from 'react';
import { Link } from 'react-router-dom';

interface Props {
  className: string;
  onClick: () => void;
}

const OrganizationSettingsButton: React.ForwardRefRenderFunction<HTMLAnchorElement, Props> =
  ({ className, onClick }, ref) => {
    return (
      <Link ref={ref} className={className} to={ORGANIZATION_SETTINGS_PAGE_PATH} onClick={onClick}>
        <Icon name="settings" size="small" space="after" />
        {'Organization settings'}
      </Link>
    );
  };

export default React.forwardRef(OrganizationSettingsButton);
