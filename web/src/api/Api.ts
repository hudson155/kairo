import axios, { Axios } from 'axios';
import env from 'env';
import { selector } from 'recoil';

export interface Request {
  method: 'GET',
  path: string,
  qp?: URLSearchParams
  body?: any
}

export default class Api {
  private readonly axios: Axios = axios.create({
    baseURL: env.limber.apiBaseUrl,
    timeout: 10_000,
    validateStatus: (status) => (status >= 200 && status < 300) || status === 404,
  });

  async request<T>(request: Request): Promise<T> {
    const response = await this.axios.request({
      url: request.path,
      headers: this.headers(request),
      params: request.qp,
      data: request.body,
    });
    if (response.status === 404) return undefined as T;
    return response.data;
  }

  private headers(request: Request): Record<string, string> {
    const result: Record<string, string> = {
      'Accept': 'application/json',
    };
    if (request.body) result['Content-Type'] = 'application/json';
    return result;
  }
}

export const apiState = selector<Api>({
  key: 'api/api',
  get: () => new Api(),
});
