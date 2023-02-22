import { getHref } from 'metadata';
import React, { ReactNode, useEffect } from 'react';
import { useRecoilValue } from 'recoil';
import auth0ClientState from 'state/auth/auth0Client';
import isAuthenticatedState from 'state/auth/isAuthenticated';

interface Props {
  children: ReactNode;
}

const AuthenticationRequired: React.FC<Props> = ({ children }) => {
  const auth0 = useRecoilValue(auth0ClientState);
  const isAuthenticated = useRecoilValue(isAuthenticatedState);

  useEffect(() => {
    if (!isAuthenticated) {
      void auth0.loginWithRedirect({
        appState: {
          returnTo: getHref(),
        },
        authorizationParams: {
          redirectMethod: 'replace',
          screen_hint: 'login', // eslint-disable-line @typescript-eslint/naming-convention
        },
      });
    }
  }, [auth0, isAuthenticated]);

  if (!isAuthenticated) return null;

  return <>{children}</>;
};

export default AuthenticationRequired;
