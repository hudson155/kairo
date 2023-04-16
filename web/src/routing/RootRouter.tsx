import AuthenticationRequired from 'app/AuthenticationRequired';
import loginRoute from 'page/login/loginRoute';
import logoutRoute from 'page/logout/logoutRoute';
import React from 'react';
import { Route, Routes } from 'react-router-dom';
import AuthenticatedRouter from 'routing/AuthenticatedRouter';

const RootRouter: React.FC = () => {
  const featureRouter = (
    <AuthenticationRequired>
      <AuthenticatedRouter />
    </AuthenticationRequired>
  );

  return (
    <Routes>
      {loginRoute.route}
      {logoutRoute.route}
      <Route element={featureRouter} path="/*" />
    </Routes>
  );
};

export default RootRouter;
