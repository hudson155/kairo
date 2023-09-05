import Api, { apiState } from 'api/Api';
import { selector } from 'recoil';
import OrganizationRep from 'rep/OrganizationRep';

class OrganizationApi {
  private readonly api: Api;

  constructor(api: Api) {
    this.api = api;
  }

  async get(organizationId: string): Promise<OrganizationRep | null> {
    return await this.api.request<OrganizationRep | null>({
      method: 'GET',
      path: `/organizations/${organizationId}`,
    });
  }

  async listAll(): Promise<OrganizationRep[]> {
    return await this.api.request<OrganizationRep[]>({
      method: 'GET',
      path: '/organizations',
    });
  }

  async create(creator: OrganizationRep.Creator): Promise<OrganizationRep> {
    return await this.api.request<OrganizationRep, OrganizationRep.Creator>({
      method: 'POST',
      path: '/organizations',
      body: creator,
    });
  }

  async update(organizationId: string, updater: OrganizationRep.Updater): Promise<OrganizationRep> {
    return await this.api.request<OrganizationRep, OrganizationRep.Updater>({
      method: 'PATCH',
      path: `/organizations/${organizationId}`,
      body: updater,
    });
  }

  async delete(organizationId: string): Promise<OrganizationRep> {
    return await this.api.request<OrganizationRep>({
      method: 'DELETE',
      path: `/organizations/${organizationId}`,
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
