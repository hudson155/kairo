import LoginPage from 'page/loginPage/LoginPage';
import { ReactNode } from 'react';
import { Route } from 'react-router-dom';

const loginRoute = (): ReactNode => <Route element={<LoginPage />} path="/login" />;

export default loginRoute;
