import AuthenticationRequired from 'app/AuthenticationRequired';
import { loginRoute } from 'page/LoginPage';
import { logoutRoute } from 'page/LogoutPage';
import React from 'react';
import { Route, Routes } from 'react-router-dom';
import FeatureRouter from 'routing/FeatureRouter';

const RootRouter: React.FC = () => {
  const featureRouter = (
    <AuthenticationRequired>
      <FeatureRouter />
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
