import React, { useEffect } from 'react';
import { useRecoilValue } from 'recoil';
import auth0ClientState from 'state/auth/auth0Client';

const LoginPage: React.FC = () => {
  const auth0 = useRecoilValue(auth0ClientState);

  useEffect(() => {
    // noinspection JSIgnoredPromiseFromCall
    auth0.loginWithRedirect({
      authorizationParams: {
        prompt: 'login',
        redirectMethod: 'replace',
        screen_hint: 'login',
      },
    });
  }, [auth0]);

  return null;
};

export default LoginPage;
