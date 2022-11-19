import AuthenticationRequired from 'app/AuthenticationRequired';
import { loginRoute } from 'page/login/LoginRoute';
import { logoutRoute } from 'page/logout/LogoutRoute';
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
      {loginRoute()}
      {logoutRoute()}
      <Route element={featureRouter} path="/*" />
    </Routes>
  );
};

export default RootRouter;
