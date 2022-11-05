import { loginRoute } from 'page/LoginPage';
import { logoutRoute } from 'page/LogoutPage';
import React from 'react';
import { Route, Routes } from 'react-router-dom';
import FeatureRouter from './FeatureRouter';

const RootRouter: React.FC = () => {
  return (
    <Routes>
      {loginRoute()}
      {logoutRoute()}
      <Route element={<FeatureRouter />} path="/*" />
    </Routes>
  );
};

export default RootRouter;
