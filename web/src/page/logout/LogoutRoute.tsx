import LogoutPage from 'page/logout/LogoutPage';
import { ReactNode } from 'react';
import { Route } from 'react-router-dom';

const LOGOUT_PAGE_PATH = '/logout';

export const logoutRoute = (): ReactNode => <Route element={<LogoutPage />} path={LOGOUT_PAGE_PATH} />;
