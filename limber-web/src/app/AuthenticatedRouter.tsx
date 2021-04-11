import React from 'react';
import LimberApiProvider from '../limberApi/LimberApiProvider';
import OrgProvider from '../provider/OrgProvider';
import LoadingPage from './pages/LoadingPage/LoadingPage';

/**
 * TODO: Implement this properly.
 */
const AuthenticatedRouter: React.FC = () => {
  return (
    <LimberApiProvider.Authenticated>
      <OrgProvider fallback={<LoadingPage debugMessage="Loading org." />}>
        <h1>Limber</h1>
        <p>This is Limber.</p>
      </OrgProvider>
    </LimberApiProvider.Authenticated>
  );
};

export default AuthenticatedRouter;
