import axios, { AxiosInstance, AxiosResponse } from 'axios';
import { TenantRepComplete } from '../rep/Tenant';
import { OrgRepComplete } from '../rep/Org';

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
    const result = await this.request<OrgRepComplete>('GET', url);
    return result?.data;
  }

  async getTenant(tenantDomain: string): Promise<TenantRepComplete | undefined> {
    const url = '/tenants';
    const params = { domain: tenantDomain };
    const result = await this.request<TenantRepComplete>('GET', url, params);
    return result?.data;
  }

  private async headers(): Promise<any> {
    const jwt = await this.getJwt();
    const headers: any = {};
    if (jwt) headers['Authorization'] = `Bearer ${jwt}`;
    return headers;
  }

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
