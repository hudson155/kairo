import { organizationAuthApiState } from 'api/OrganizationAuthApi';
import { useRecoilValue, useSetRecoilState } from 'recoil';
import OrganizationAuthRep from 'rep/OrganizationAuthRep';
import organizationAuthState from 'state/core/organizationAuth';
import organizationAuthIdState from 'state/core/organizationAuthId';
import organizationAuthsState from 'state/core/organizationAuthsState';

type UpdateOrganizationAuth = (updater: OrganizationAuthRep.Updater) => Promise<OrganizationAuthRep>;

const useUpdateOrganizationAuth = (organizationId: string, authId: string): UpdateOrganizationAuth => {
  const authApi = useRecoilValue(organizationAuthApiState({ authenticated: true }));

  const setAuth = useSetRecoilState(organizationAuthsState(organizationId));

  const currentAuthId = useRecoilValue(organizationAuthIdState);
  const setCurrentAuth = useSetRecoilState(organizationAuthState);

  return async (updater): Promise<OrganizationAuthRep> => {
    const auth = await authApi.update(authId, updater);
    setAuth(auth);
    if (authId === currentAuthId) {
      setCurrentAuth(auth);
    }
    return auth;
  };
};

export default useUpdateOrganizationAuth;
