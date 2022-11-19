import LoginPage from 'page/login/LoginPage';
import { ReactNode } from 'react';
import { Route } from 'react-router-dom';

const LOGIN_PAGE_PATH = `/login`;

export const loginRoute = (): ReactNode => <Route element={<LoginPage />} path={LOGIN_PAGE_PATH} />;
