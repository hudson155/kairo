import { getRootUrl } from 'metadata';
import React, { useEffect } from 'react';
import { useRecoilValue } from 'recoil';
import auth0ClientState from 'state/auth/auth0Client';

const LogoutPage: React.FC = () => {
  const auth0 = useRecoilValue(auth0ClientState);

  useEffect(() => {
    void auth0.logout({
      logoutParams: {
        returnTo: getRootUrl(),
      },
    });
  }, [auth0]);

  return null;
};

export default LogoutPage;
