import React from 'react';
import { BrowserRouter } from 'react-router-dom';
import RootRouter from 'routing/RootRouter';

const AppDelegate: React.FC = () => {
  return (
    <BrowserRouter>
      <RootRouter />
    </BrowserRouter>
  );
};

export default AppDelegate;
