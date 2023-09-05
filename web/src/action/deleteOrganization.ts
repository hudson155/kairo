import { organizationApiState } from 'api/OrganizationApi';
import UserError from 'error/UserError';
import { useRecoilValue } from 'recoil';
import OrganizationRep from 'rep/OrganizationRep';
import organizationsState from 'state/admin/organizations';
import organizationIdState from 'state/core/organizationId';
import { useSetRecoilStateIfActive } from 'state/util/useSetRecoilStateIfActive';

type DeleteOrganization = () => Promise<OrganizationRep>;

const useDeleteOrganization = (organizationId: string): DeleteOrganization => {
  const organizationApi = useRecoilValue(organizationApiState);

  const setOrganizations = useSetRecoilStateIfActive(organizationsState);

  const currentOrganizationId = useRecoilValue(organizationIdState);

  return async (): Promise<OrganizationRep> => {
    if (organizationId === currentOrganizationId) {
      throw new UserError('Can\'t delete the auth belonging to the current organization.');
    }
    const organization = await organizationApi.delete(organizationId);
    setOrganizations((currVal) => {
      const newVal = new Map(currVal);
      newVal.delete(organization.id);
      return newVal;
    });
    return organization;
  };
};

export default useDeleteOrganization;
