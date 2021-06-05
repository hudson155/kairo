import React from 'react';
import LimberApiProvider from '../limberApi/LimberApiProvider';
import AuthenticatedStateProvider from '../provider/AuthenticatedStateProvider';
import TopLevelNavbar from './components/TopLevelNavbar';
import FeatureRouter from './FeatureRouter';

const AuthenticatedRouter: React.FC = () => {
  return (
    <LimberApiProvider.Authenticated>
      <AuthenticatedStateProvider>
        <TopLevelNavbar />
        <FeatureRouter />
      </AuthenticatedStateProvider>
    </LimberApiProvider.Authenticated>
  );
};

export default AuthenticatedRouter;
