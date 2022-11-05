import React, { ReactNode, useEffect } from 'react';
import { Route } from 'react-router-dom';
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

export const loginRoute = (): ReactNode => <Route element={<LoginPage />} path="/login" />;
