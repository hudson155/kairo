import { organizationAuthApiState } from 'api/OrganizationAuthApi';
import { useRecoilValue, useSetRecoilState } from 'recoil';
import OrganizationAuthRep from 'rep/OrganizationAuthRep';
import organizationAuthsState from 'state/core/organizationAuthsState';

type CreateOrganizationAuth = (creator: OrganizationAuthRep.Creator) => Promise<OrganizationAuthRep>;

const useCreateOrganizationAuth = (organizationGuid: string): CreateOrganizationAuth => {
  const authApi = useRecoilValue(organizationAuthApiState({ authenticated: true }));

  const setAuth = useSetRecoilState(organizationAuthsState(organizationGuid));

  return async (creator): Promise<OrganizationAuthRep> => {
    const auth = await authApi.create(organizationGuid, creator);
    setAuth(auth);
    return auth;
  };
};

export default useCreateOrganizationAuth;
