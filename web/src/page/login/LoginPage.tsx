import React, { useEffect } from 'react';
import { useRecoilValue } from 'recoil';
import auth0ClientState from 'state/auth/auth0Client';

const LoginPage: React.FC = () => {
  const auth0 = useRecoilValue(auth0ClientState);

  useEffect(() => {
    void auth0.loginWithRedirect({
      authorizationParams: {
        prompt: 'login',
        redirectMethod: 'replace',
        screen_hint: 'login', // eslint-disable-line @typescript-eslint/naming-convention
      },
    });
  }, [auth0]);

  return null;
};

export default LoginPage;
