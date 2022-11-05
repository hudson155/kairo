import loginRoute from 'page/loginPage/loginRoute';
import logoutRoute from 'page/logoutPage/logoutRoute';
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
