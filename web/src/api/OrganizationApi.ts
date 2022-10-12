import Api, { apiState } from 'api/Api';
import { selector } from 'recoil';
import OrganizationRep from 'rep/OrganizationRep';

class OrganizationApi {
  private readonly api: Api;

  constructor(api: Api) {
    this.api = api;
  }

  async getByHostname(hostname: string): Promise<OrganizationRep | undefined> {
    const path = '/organizations';
    const qp = new URLSearchParams({ hostname });
    return this.api.request<OrganizationRep | undefined>({ method: 'GET', path, qp });
  }
}

export const organizationApiState = selector<OrganizationApi>({
  key: 'api/organization',
  get: ({ get }) => new OrganizationApi(get(apiState)),
});
