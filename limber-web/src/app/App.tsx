import React from 'react';
import { BrowserRouter } from 'react-router-dom';
import AuthProvider from '../auth/AuthProvider';
import LimberApiProvider from '../limberApi/LimberApiProvider';
import TenantProvider from '../provider/TenantProvider';
import LimberThemeProvider from '../theme/LimberThemeProvider';
import ErrorBoundary from './components/ErrorBoundary';
import ErrorPage from './pages/ErrorPage';
import LoadingPage from './pages/LoadingPage';
import RootRouter from './RootRouter';

/**
 * The root of the whole app.
 */
const App: React.FC = () => {
  return (
    <LimberThemeProvider>
      <ErrorBoundary fallback={error => <ErrorPage error={error} />}>
        <BrowserRouter>
          <LimberApiProvider.Unauthenticated>
            <TenantProvider fallback={<LoadingPage debugMessage="Loading tenant." />}>
              <AuthProvider fallback={<LoadingPage debugMessage="Identifying with Auth0." />}>
                <RootRouter />
              </AuthProvider>
            </TenantProvider>
          </LimberApiProvider.Unauthenticated>
        </BrowserRouter>
      </ErrorBoundary>
    </LimberThemeProvider>
  );
};

export default App;
