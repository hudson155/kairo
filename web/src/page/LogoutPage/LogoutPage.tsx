import { rootUrl } from 'metadata';
import React, { useEffect } from 'react';
import { useRecoilValue } from 'recoil';
import auth0ClientState from 'state/auth/auth0Client';

const LogoutPage: React.FC = () => {
  const auth0 = useRecoilValue(auth0ClientState);

  useEffect(() => {
    // noinspection JSIgnoredPromiseFromCall
    auth0.logout({
      logoutParams: {
        returnTo: rootUrl,
      },
    });
  }, [auth0.logout]);

  return null;
};

export default LogoutPage;
