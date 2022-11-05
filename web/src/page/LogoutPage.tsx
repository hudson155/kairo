import { rootUrl } from 'metadata';
import React, { ReactNode, useEffect } from 'react';
import { Route } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import auth0ClientState from 'state/auth/auth0Client';

const LogoutPage: React.FC = () => {
  const auth0 = useRecoilValue(auth0ClientState);

  useEffect(() => {
    void auth0.logout({
      logoutParams: {
        returnTo: rootUrl,
      },
    });
  }, [auth0]);

  return null;
};

export default LogoutPage;

export const logoutRoute = (): ReactNode => <Route element={<LogoutPage />} path="/logout" />;
