import TenantRep from '../rep/TenantRep';
import LimberBaseApi from './LimberBaseApi';

export default class TenantApi {
  private readonly api: LimberBaseApi;

  constructor(api: LimberBaseApi) {
    this.api = api;
  }

  async getByDomain(tenantDomain: string): Promise<TenantRep.Complete | undefined> {
    const url = '/tenants';
    const params = { domain: tenantDomain };
    return await this.api.request<TenantRep.Complete>('GET', url, params);
  }
}
