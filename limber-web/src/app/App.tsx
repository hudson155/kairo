import React from 'react';
import AuthProvider from '../auth/AuthProvider';
import { LimberApiProvider } from '../limberApi/LimberApiProvider';
import TenantProvider from '../provider/TenantProvider';
import LimberThemeProvider from '../theme/LimberThemeProvider';
import ErrorBoundary from './components/ErrorBoundary/ErrorBoundary';
import ErrorPage from './pages/ErrorPage/ErrorPage';
import LoadingPage from './pages/LoadingPage/LoadingPage';
import RootRouter from './RootRouter';

const App: React.FC = () => {
  return (
    <LimberThemeProvider>
      <ErrorBoundary fallback={error => <ErrorPage error={error} />}>
        <LimberApiProvider.Unauthenticated>
          <TenantProvider fallback={<LoadingPage debugMessage="Loading tenant." />}>
            <AuthProvider>
              <RootRouter />
            </AuthProvider>
          </TenantProvider>
        </LimberApiProvider.Unauthenticated>
      </ErrorBoundary>
    </LimberThemeProvider>
  );
};

export default App;
