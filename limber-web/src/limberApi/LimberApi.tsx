import axios, { AxiosInstance } from 'axios';
import React from 'react';
import { TenantRep } from '../rep/TenantRep';

type HttpMethod = 'DELETE' | 'GET' | 'PATCH' | 'POST' | 'PUT';

export default class LimberApi {
  private readonly client: AxiosInstance;
  private readonly getJwt: () => Promise<string | undefined>;
  private readonly additionalApiLatency: number;

  constructor(
    baseUrl: string,
    getJwt: () => Promise<string | undefined>,
    additionalApiLatency: number,
  ) {
    this.client = axios.create({ baseURL: baseUrl });
    this.getJwt = getJwt;
    this.additionalApiLatency = additionalApiLatency;
  }

  async getTenant(tenantDomain: string): Promise<TenantRep | undefined> {
    const url = '/tenants';
    const params = { domain: tenantDomain };
    return await this.request<TenantRep>('GET', url, params);
  }

  private async request<T>(method: HttpMethod, url: string, params: any = {}): Promise<T | undefined> {
    return new Promise<T | undefined>(resolve => {
      setTimeout(async () => {
        try {
          const headers = await this.headers();
          resolve((await this.client.request<T>({ method, url, params, headers })).data);
        } catch (e) {
          if (e.response?.status === 404) {
            resolve(undefined);
            return;
          }
          throw e;
        }
      }, this.additionalApiLatency);
    });

  }

  private async headers(): Promise<any> {
    const jwt = await this.getJwt();
    const headers: any = {};
    if (jwt) headers['Authorization'] = `Bearer ${jwt}`;
    return headers;
  }
}
