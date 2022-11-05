import LogoutPage from 'page/logoutPage/LogoutPage';
import { ReactNode } from 'react';
import { Route } from 'react-router-dom';

const logoutRoute = (): ReactNode => <Route element={<LogoutPage />} path="/logout" />;

export default logoutRoute;
