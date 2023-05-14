import { organizationAuthApiState } from 'api/OrganizationAuthApi';
import { useRecoilValue, useSetRecoilState } from 'recoil';
import OrganizationAuthRep from 'rep/OrganizationAuthRep';
import organizationAuthState from 'state/core/organizationAuth';
import organizationAuthGuidState from 'state/core/organizationAuthGuidState';
import organizationAuthsState from 'state/core/organizationAuthsState';

type UpdateOrganizationAuth = (updater: OrganizationAuthRep.Updater) => Promise<OrganizationAuthRep>;

const useUpdateOrganizationAuth = (organizationGuid: string, authGuid: string): UpdateOrganizationAuth => {
  const authApi = useRecoilValue(organizationAuthApiState({ authenticated: true }));

  const setAuth = useSetRecoilState(organizationAuthsState(organizationGuid));

  const currentAuthGuid = useRecoilValue(organizationAuthGuidState);
  const setCurrentAuth = useSetRecoilState(organizationAuthState);

  return async (updater): Promise<OrganizationAuthRep> => {
    const auth = await authApi.update(authGuid, updater);
    setAuth(auth);
    if (authGuid === currentAuthGuid) {
      setCurrentAuth(auth);
    }
    return auth;
  };
};

export default useUpdateOrganizationAuth;
