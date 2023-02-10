import React, { ReactNode } from 'react';
import { Route } from 'react-router-dom';

const OrganizationSettingsPage = React.lazy(() => import('page/organizationSettings/OrganizationSettingsPage'));

export const ORGANIZATION_SETTINGS_PAGE_PATH = '/settings';

export const organizationSettingsRoute = (): ReactNode => {
  return <Route element={<OrganizationSettingsPage />} path={ORGANIZATION_SETTINGS_PAGE_PATH} />;
};
