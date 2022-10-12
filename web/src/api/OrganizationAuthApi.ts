import Api, { apiState } from 'api/Api';
import { selector } from 'recoil';
import OrganizationAuthRep from 'rep/OrganizationAuthRep';

class OrganizationAuthApi {
  private readonly api: Api;

  constructor(api: Api) {
    this.api = api;
  }

  async getByOrganization(organizationGuid: string): Promise<OrganizationAuthRep | undefined> {
    const path = `/organizations/${organizationGuid}/auth`;
    return this.api.request<OrganizationAuthRep | undefined>({ method: 'GET', path });
  }
}

export const organizationAuthApiState = selector<OrganizationAuthApi>({
  key: 'api/organizationAuth',
  get: ({ get }) => new OrganizationAuthApi(get(apiState)),
});
