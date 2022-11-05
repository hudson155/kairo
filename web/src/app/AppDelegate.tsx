import React from 'react';
import { HelmetProvider } from 'react-helmet-async';
import { BrowserRouter } from 'react-router-dom';
import RootRouter from 'routing/RootRouter';

const AppDelegate: React.FC = () => {
  return (
    <HelmetProvider>
      <BrowserRouter>
        <RootRouter />
      </BrowserRouter>
    </HelmetProvider>
  );
};

export default AppDelegate;
