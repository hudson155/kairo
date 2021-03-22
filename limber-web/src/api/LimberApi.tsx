import axios, { AxiosInstance, AxiosResponse } from 'axios';
import React from 'react';

type HttpMethod = 'DELETE' | 'GET' | 'PATCH' | 'POST' | 'PUT';

export default class LimberApi {
  private readonly client: AxiosInstance;
  private readonly getJwt: () => Promise<string | undefined>;

  constructor(baseUrl: string, getJwt: () => Promise<string | undefined>) {
    this.client = axios.create({ baseURL: baseUrl });
    this.getJwt = getJwt;
  }

  private async request<T>(method: HttpMethod, url: string, params: any = {}): Promise<AxiosResponse<T> | undefined> {
    try {
      const headers = await this.headers();
      return await this.client.request<T>({ method, url, params, headers });
    } catch (e) {
      if (e.response?.status === 404) return undefined;
      throw e;
    }
  }

  private async headers(): Promise<any> {
    const jwt = await this.getJwt();
    const headers: any = {};
    if (jwt) headers['Authorization'] = `Bearer ${jwt}`;
    return headers;
  }
}
