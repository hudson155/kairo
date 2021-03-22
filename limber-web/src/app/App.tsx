import React, { ReactNode, Suspense } from 'react';
import { BrowserRouter } from 'react-router-dom';
import { UnauthenticatedLimberApiProvider } from '../limberApi/LimberApiProvider';
import AuthProvider from '../provider/AuthProvider';
import TenantProvider, { useTenant } from '../provider/TenantProvider';
import LimberThemeProvider from '../theme/LimberThemeProvider';
import ErrorPage from './pages/ErrorPage/ErrorPage';
import LoadingPage from './pages/LoadingPage/LoadingPage';

const App: React.FC = () => {
  return (
    <LimberThemeProvider>
      <UnauthenticatedLimberApiProvider>
        <BrowserRouter>
          <TenantProvider>
            <ErrorBoundary fallback={<ErrorPage debugMessage="Tenant failed to load." />}>
              <Suspense fallback={<LoadingPage debugMessage="Loading tenant." />}>
                <LoadedApp />
              </Suspense>
            </ErrorBoundary>
          </TenantProvider>
        </BrowserRouter>
      </UnauthenticatedLimberApiProvider>
    </LimberThemeProvider>
  );
};

export default App;

const LoadedApp: React.FC = () => {
  const tenant = useTenant().read()!; // TODO: No bang

  return <AuthProvider auth0ClientId={tenant.auth0ClientId}>
    <h1>Limber</h1>
    <p>This is Limber.</p>
  </AuthProvider>;
};

interface ErrorBoundaryProps {
  readonly fallback: ReactNode
}

class ErrorBoundary extends React.Component<ErrorBoundaryProps> {
  state = { hasError: false, error: null };

  static getDerivedStateFromError(error: any) {
    console.log('got error');
    return {
      hasError: true,
      error,
    };
  }

  render() {
    if (this.state.hasError) {
      return this.props.fallback;
    }
    return this.props.children;
  }
}
