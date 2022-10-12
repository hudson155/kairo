import loginRoute from 'page/LoginPage/loginRoute';
import logoutRoute from 'page/LogoutPage/logoutRoute';
import React from 'react';
import { Routes } from 'react-router-dom';

const RootRouter: React.FC = () => {
  return (
    <Routes>
      {loginRoute()}
      {logoutRoute()}
    </Routes>
  );
};

export default RootRouter;
