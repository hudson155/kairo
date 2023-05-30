import { organizationApiState } from 'api/OrganizationApi';
import { useRecoilValue, useSetRecoilState } from 'recoil';
import OrganizationRep from 'rep/OrganizationRep';
import organizationsState from 'state/admin/organizations';
import organizationState from 'state/core/organization';
import organizationIdState from 'state/core/organizationId';
import { useSetRecoilStateIfActive } from 'state/util/useSetRecoilStateIfActive';

type UpdateOrganization = (updater: OrganizationRep.Updater) => Promise<OrganizationRep>;

const useUpdateOrganization = (organizationId: string): UpdateOrganization => {
  const organizationApi = useRecoilValue(organizationApiState);

  const setOrganizations = useSetRecoilStateIfActive(organizationsState);

  const currentOrganizationId = useRecoilValue(organizationIdState);
  const setCurrentOrganization = useSetRecoilState(organizationState);

  return async (updater): Promise<OrganizationRep> => {
    const organization = await organizationApi.update(organizationId, updater);
    setOrganizations((currVal) => new Map(currVal).set(organization.id, organization));
    if (organizationId === currentOrganizationId) {
      setCurrentOrganization(organization);
    }
    return organization;
  };
};

export default useUpdateOrganization;
