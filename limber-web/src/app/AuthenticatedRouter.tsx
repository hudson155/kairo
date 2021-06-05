import React from 'react';
import LimberApiProvider from '../limberApi/LimberApiProvider';
import OrgProvider from '../provider/OrgProvider';
import TopLevelNavbar from './components/TopLevelNavbar';
import FeatureRouter from './FeatureRouter';
import LoadingPage from './pages/LoadingPage';

const AuthenticatedRouter: React.FC = () => {
  return (
    <LimberApiProvider.Authenticated>
      <OrgProvider fallback={<LoadingPage debugMessage="Loading org." />}>
        <TopLevelNavbar />
        <FeatureRouter />
      </OrgProvider>
    </LimberApiProvider.Authenticated>
  );
};

export default AuthenticatedRouter;
