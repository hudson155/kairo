import React, { ReactNode } from 'react';
import { Route } from 'react-router-dom';

const AdminSettingsPage = React.lazy(() => import('page/adminSettings/AdminSettingsPage'));

export const ADMIN_SETTINGS_PAGE_PATH = '/admin';

export const adminSettingsRoute = (): ReactNode => {
  return <Route element={<AdminSettingsPage />} path={ADMIN_SETTINGS_PAGE_PATH} />;
};
