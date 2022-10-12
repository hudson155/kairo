import loginRoute from 'page/LoginPage/loginRoute';
import logoutRoute from 'page/LogoutPage/logoutRoute';
import React from 'react';
import { Route, Routes } from 'react-router-dom';
import FeatureRouter from './FeatureRouter';

const RootRouter: React.FC = () => {
  return (
    <Routes>
      {loginRoute()}
      {logoutRoute()}

      <Route path="/*" element={<FeatureRouter />} />
    </Routes>
  );
};

export default RootRouter;
