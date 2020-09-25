import axios, { AxiosInstance, AxiosResponse } from 'axios';

import { OrgRepComplete } from '../rep/Org';
import { TenantRepComplete } from '../rep/Tenant';

type Method = 'DELETE' | 'GET' | 'PATCH' | 'POST' | 'PUT';

export class LimberApi {
  private readonly client: AxiosInstance;
  private readonly getJwt: () => Promise<string | undefined>;

  constructor(baseUrl: string, getJwt: () => Promise<string | undefined>) {
    this.client = axios.create({ baseURL: baseUrl });
    this.getJwt = getJwt;
  }

  async getOrg(orgGuid: string): Promise<OrgRepComplete | undefined> {
    const url = `/orgs/${encodeURIComponent(orgGuid)}`;
    const response = await this.request<OrgRepComplete>('GET', url);
    return response?.data;
  }

  async getTenant(tenantDomain: string): Promise<TenantRepComplete | undefined> {
    const url = '/tenants';
    const params = { domain: tenantDomain };
    const response = await this.request<TenantRepComplete>('GET', url, params);
    return response?.data;
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  private async headers(): Promise<any> {
    const jwt = await this.getJwt();
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    const headers: any = {};
    if (jwt) headers['Authorization'] = `Bearer ${jwt}`;
    return headers;
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  private async request<T>(method: Method, url: string, params: any = {}): Promise<AxiosResponse<T> | undefined> {
    try {
      const headers = await this.headers();
      return await this.client.request<T>({ method, url, params, headers });
    } catch (e) {
      if (e.response?.status === 404) return undefined;
      throw e;
    }
  }
}
