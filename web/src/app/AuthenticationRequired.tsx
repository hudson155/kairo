import { getHref } from 'metadata';
import React, { ReactNode, useEffect } from 'react';
import { useRecoilValue } from 'recoil';
import auth0ClientState, { AppState } from 'state/global/auth/auth0Client';
import isAuthenticatedState from 'state/global/auth/isAuthenticated';

interface Props {
  children: ReactNode;
}

const AuthenticationRequired: React.FC<Props> = ({ children }) => {
  const auth0 = useRecoilValue(auth0ClientState);
  const isAuthenticated = useRecoilValue(isAuthenticatedState);

  useEffect(() => {
    if (!isAuthenticated) {
      const state: AppState = { returnTo: getHref() };
      void auth0.loginWithRedirect({
        appState: state,
        authorizationParams: {
          redirectMethod: 'replace',
        },
      });
    }
  }, [auth0, isAuthenticated]);

  if (!isAuthenticated) return null;

  return <>{children}</>;
};

export default AuthenticationRequired;
