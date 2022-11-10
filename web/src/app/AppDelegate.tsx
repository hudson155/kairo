import ErrorBoundary from 'page/errorPage/ErrorBoundary';
import ErrorPage from 'page/errorPage/ErrorPage';
import React from 'react';
import { HelmetProvider } from 'react-helmet-async';
import { BrowserRouter } from 'react-router-dom';
import RootRouter from 'routing/RootRouter';

const AppDelegate: React.FC = () => {
  return (
    <HelmetProvider>
      <ErrorBoundary fallback={ErrorPage.fallback}>
        <BrowserRouter>
          <RootRouter />
        </BrowserRouter>
      </ErrorBoundary>
    </HelmetProvider>
  );
};

export default AppDelegate;
