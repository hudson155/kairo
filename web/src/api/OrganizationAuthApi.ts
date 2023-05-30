import Api, { apiState } from 'api/Api';
import { selectorFamily } from 'recoil';
import OrganizationAuthRep from 'rep/OrganizationAuthRep';

class OrganizationAuthApi {
  private readonly api: Api;

  constructor(api: Api) {
    this.api = api;
  }

  async get(organizationId: string): Promise<OrganizationAuthRep | null> {
    return await this.api.request<OrganizationAuthRep | null>({
      method: 'GET',
      path: `/organizations/${organizationId}/auths`,
    });
  }

  async getByHostname(hostname: string): Promise<OrganizationAuthRep | null> {
    return await this.api.request<OrganizationAuthRep | null>({
      method: 'GET',
      path: '/organization-auths',
      qp: new URLSearchParams({ hostname }),
    });
  }

  async create(organizationId: string, creator: OrganizationAuthRep.Creator): Promise<OrganizationAuthRep> {
    return await this.api.request<OrganizationAuthRep, OrganizationAuthRep.Creator>({
      method: 'POST',
      path: `/organizations/${organizationId}/auths`,
      body: creator,
    });
  }

  async update(authId: string, updater: OrganizationAuthRep.Updater): Promise<OrganizationAuthRep> {
    return await this.api.request<OrganizationAuthRep, OrganizationAuthRep.Updater>({
      method: 'PATCH',
      path: `/organization-auths/${authId}`,
      body: updater,
    });
  }

  async delete(authId: string): Promise<OrganizationAuthRep> {
    return await this.api.request<OrganizationAuthRep>({
      method: 'DELETE',
      path: `/organization-auths/${authId}`,
    });
  }
}

export const organizationAuthApiState = selectorFamily<OrganizationAuthApi, { authenticated: boolean; }>({
  key: 'api/organizationAuth',
  get: ({ authenticated }) => ({ get }) => {
    const api = get(apiState({ authenticated }));
    return new OrganizationAuthApi(api);
  },
});
