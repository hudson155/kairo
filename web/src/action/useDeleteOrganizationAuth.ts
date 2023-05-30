import { organizationAuthApiState } from 'api/OrganizationAuthApi';
import UserError from 'error/UserError';
import { useRecoilValue, useSetRecoilState } from 'recoil';
import OrganizationAuthRep from 'rep/OrganizationAuthRep';
import organizationAuthIdState from 'state/core/organizationAuthId';
import organizationAuthsState from 'state/core/organizationAuthsState';

type DeleteOrganizationAuth = () => Promise<OrganizationAuthRep>;

const useDeleteOrganizationAuth = (organizationId: string, authId: string): DeleteOrganizationAuth => {
  const authApi = useRecoilValue(organizationAuthApiState({ authenticated: true }));

  const setAuth = useSetRecoilState(organizationAuthsState(organizationId));

  const currentAuthId = useRecoilValue(organizationAuthIdState);

  return async (): Promise<OrganizationAuthRep> => {
    if (authId === currentAuthId) {
      throw new UserError('Can\'t delete the auth belonging to the current organization.');
    }
    const auth = await authApi.delete(authId);
    setAuth(undefined);
    return auth;
  };
};

export default useDeleteOrganizationAuth;
