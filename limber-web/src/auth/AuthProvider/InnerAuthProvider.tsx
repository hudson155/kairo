import { useAuth0 } from '@auth0/auth0-react';
import React, { ReactNode } from 'react';

interface Props {
  readonly fallback: ReactNode;
}

const InnerAuthProvider: React.FC<Props> = ({ fallback, children }) => {
  const auth = useAuth0();

  if (auth.isLoading) return <>{fallback}</>;
  return <>{children}</>;
};

export default InnerAuthProvider;
