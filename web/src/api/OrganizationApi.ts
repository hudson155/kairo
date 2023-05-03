import Api, { apiState } from 'api/Api';
import { selector } from 'recoil';
import OrganizationRep from 'rep/OrganizationRep';

class OrganizationApi {
  private readonly api: Api;

  constructor(api: Api) {
    this.api = api;
  }

  async get(organizationGuid: string): Promise<OrganizationRep | null> {
    return await this.api.request<OrganizationRep | null>({
      method: 'GET',
      path: `/organizations/${organizationGuid}`,
    });
  }

  async listAll(): Promise<OrganizationRep[]> {
    return await this.api.request<OrganizationRep[]>({
      method: 'GET',
      path: '/organizations',
    });
  }

  async update(organizationGuid: string, updater: OrganizationRep.Updater): Promise<OrganizationRep> {
    return await this.api.request<OrganizationRep, OrganizationRep.Updater>({
      method: 'PATCH',
      path: `/organizations/${organizationGuid}`,
      body: updater,
    });
  }
}

export const organizationApiState = selector<OrganizationApi>({
  key: 'api/organization',
  get: ({ get }) => {
    const api = get(apiState({ authenticated: true }));
    return new OrganizationApi(api);
  },
});
