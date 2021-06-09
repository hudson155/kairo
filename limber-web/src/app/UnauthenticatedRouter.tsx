import React from 'react';
import TopLevelNavbar from './components/TopLevelNavbar';

/**
 * Routing specific to unauthenticated users.
 *
 * TODO: Put something here. On the root path we can just prompt for sign in. On other paths it
 *  should show a 404-style page with the option to sign in.
 */
const UnauthenticatedRouter: React.FC = () => {
  return <TopLevelNavbar />;
};

export default UnauthenticatedRouter;
