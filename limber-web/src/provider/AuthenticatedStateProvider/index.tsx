import { Auth0ContextInterface, useAuth0 } from '@auth0/auth0-react';
import decodeJwt from 'jwt-decode';
import { JwtClaims } from 'limber-jwt';
import React, { useEffect } from 'react';
import LoadingPage from '../../app/pages/LoadingPage';
import useLoadingState from '../../hook/LoadingState';
import { useLimberApi } from '../../limberApi/LimberApiProvider';
import Jwt from '../../rep/Jwt';
import OrgRep from '../../rep/OrgRep';
import UserRep from '../../rep/UserRep';
import { checkNotUndefined } from '../../util/Util';
import { useTenant } from '../TenantProvider';
import OrgProvider from './OrgProvider';
import UserProvider from './UserProvider';

/**
 * Loads authenticated state in parallel, only rendering the component's children after it all
 * finishes. This only works if the user is authenticated.
 */
const AuthenticatedStateProvider: React.FC = ({ children }) => {
  const api = useLimberApi();
  const auth = useAuth0();
  const tenant = useTenant();

  const [org, setOrg, setOrgError] = useLoadingState<OrgRep.Complete | undefined>();
  const [user, setUser, setUserError] = useLoadingState<UserRep.Complete | undefined>();

  useEffect(() => {
    getJwt(auth).then(jwt => Promise.all([
      api.org.get(tenant.orgGuid).then(setOrg, setOrgError),
      api.user.get(jwt.user.guid).then(setUser, setUserError),
    ]));
  }, [auth, tenant.orgGuid]);

  const isLoading = org.isLoading || user.isLoading;
  if (isLoading) {
    return <LoadingPage debugMessage="Loading authenticated state." />;
  }

  return (
    <OrgProvider org={checkNotUndefined(org.get(), 'Org not found.')}>
      <UserProvider user={checkNotUndefined(user.get(), 'User not found.')}>
        {children}
      </UserProvider>
    </OrgProvider>
  );
};

export default AuthenticatedStateProvider;

async function getJwt(auth: Auth0ContextInterface): Promise<Jwt> {
  const jwtString = await auth.getAccessTokenSilently();
  const jwt = decodeJwt<any>(jwtString);
  return { user: jwt[JwtClaims.user] };
}
