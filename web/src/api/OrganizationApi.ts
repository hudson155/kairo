import Api, { apiState } from 'api/Api';
import { selector } from 'recoil';
import OrganizationRep from 'rep/OrganizationRep';

class OrganizationApi {
  private readonly api: Api;

  constructor(api: Api) {
    this.api = api;
  }

  async get(organizationGuid: string): Promise<OrganizationRep | undefined> {
    return await this.api.request<OrganizationRep | undefined>({
      method: `GET`,
      path: `/organizations/${organizationGuid}`,
    });
  }
}

export const organizationApiState = selector<OrganizationApi>({
  key: `api/organization`,
  get: ({ get }) => {
    const api = get(apiState({ authenticated: true }));
    return new OrganizationApi(api);
  },
});
