import Api, { apiState } from 'api/Api';
import { selector } from 'recoil';
import OrganizationAuthRep from 'rep/OrganizationAuthRep';

class OrganizationAuthApi {
  private readonly api: Api;

  constructor(api: Api) {
    this.api = api;
  }

  async getByHostname(hostname: string): Promise<OrganizationAuthRep | undefined> {
    const path = '/organization-auths';
    const qp = new URLSearchParams({ hostname });
    return await this.api.request<OrganizationAuthRep>({ method: 'GET', path, qp });
  }
}

export const organizationAuthApiState = selector<OrganizationAuthApi>({
  key: 'api/organizationAuth',
  get: ({ get }) => {
    const api = get(apiState({ authenticated: false }));
    return new OrganizationAuthApi(api);
  },
});
