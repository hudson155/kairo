import { organizationAuthApiState } from 'api/OrganizationAuthApi';
import UserError from 'error/UserError';
import { useRecoilValue, useSetRecoilState } from 'recoil';
import OrganizationAuthRep from 'rep/OrganizationAuthRep';
import organizationAuthGuidState from 'state/core/organizationAuthGuidState';
import organizationAuthsState from 'state/core/organizationAuthsState';

type DeleteOrganizationAuth = () => Promise<OrganizationAuthRep>;

const useDeleteOrganizationAuth = (organizationGuid: string, authGuid: string): DeleteOrganizationAuth => {
  const authApi = useRecoilValue(organizationAuthApiState({ authenticated: true }));

  const setAuth = useSetRecoilState(organizationAuthsState(organizationGuid));

  const currentAuthGuid = useRecoilValue(organizationAuthGuidState);

  return async (): Promise<OrganizationAuthRep> => {
    if (authGuid === currentAuthGuid) {
      throw new UserError('Can\'t delete the auth belonging to the current organization.');
    }
    const auth = await authApi.delete(authGuid);
    setAuth(undefined);
    return auth;
  };
};

export default useDeleteOrganizationAuth;
