import React, { ReactNode } from 'react';
import { Route } from 'react-router-dom';

const SettingsPage = React.lazy(() => import(`page/settings/SettingsPage`));

export const SETTINGS_PAGE_PATH = `/settings`;

export const settingsRoute = (): ReactNode => <Route element={<SettingsPage />} path={SETTINGS_PAGE_PATH} />;
