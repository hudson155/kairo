import Icon from 'component/icon/Icon';
import { SETTINGS_PAGE_PATH } from 'page/settings/SettingsPageRoute';
import React from 'react';
import { Link } from 'react-router-dom';

interface Props {
  className: string;
}

const TopNavMenuSettingsButton: React.ForwardRefRenderFunction<HTMLAnchorElement, Props> =
  ({ className }, ref) => {
    return (
      <Link ref={ref} className={className} to={SETTINGS_PAGE_PATH}>
        <Icon name="settings" size="small" space="after" />
        {'Organization settings'}
      </Link>
    );
  };

export default React.forwardRef(TopNavMenuSettingsButton);
