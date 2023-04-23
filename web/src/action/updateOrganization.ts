import { organizationApiState } from 'api/OrganizationApi';
import { useRecoilValue, useSetRecoilState } from 'recoil';
import OrganizationRep from 'rep/OrganizationRep';
import organizationState from 'state/global/core/organization';
import organizationGuidState from 'state/global/core/organizationGuid';

const useUpdateOrganization = (): (updater: OrganizationRep.Updater) => Promise<void> => {
  const organizationApi = useRecoilValue(organizationApiState);
  const organizationGuid = useRecoilValue(organizationGuidState);
  const setOrganization = useSetRecoilState(organizationState);

  return async (updater): Promise<void> => {
    const organization = await organizationApi.update(organizationGuid, updater);
    setOrganization(organization);
  };
};

export default useUpdateOrganization;
