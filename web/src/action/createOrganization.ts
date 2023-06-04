import { organizationApiState } from 'api/OrganizationApi';
import { useRecoilValue } from 'recoil';
import OrganizationRep from 'rep/OrganizationRep';
import organizationsState from 'state/admin/organizations';
import { useSetRecoilStateIfActive } from 'state/util/useSetRecoilStateIfActive';

type CreateOrganization = (creator: OrganizationRep.Creator) => Promise<OrganizationRep>;

const useCreateOrganization = (): CreateOrganization => {
  const organizationApi = useRecoilValue(organizationApiState);

  const setOrganizations = useSetRecoilStateIfActive(organizationsState);

  return async (creator): Promise<OrganizationRep> => {
    const organization = await organizationApi.create(creator);
    setOrganizations((organizations) => {
      return organizations.set(organization.id, organization);
    });
    return organization;
  };
};

export default useCreateOrganization;
