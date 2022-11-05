import axios, { Axios } from 'axios';
import env from 'env';
import { selector } from 'recoil';

export interface Request<Req> {
  method: 'GET';
  path: string;
  qp?: URLSearchParams;
  body?: Req;
}

export default class Api {
  private readonly axios: Axios = axios.create({
    baseURL: env.limber.apiBaseUrl, // eslint-disable-line @typescript-eslint/naming-convention
    timeout: 10_000,
    validateStatus: (status) => (status >= 200 && status < 300) || status === 404,
  });

  async request<Res, Req = undefined>(request: Request<Req>): Promise<Res> {
    const response = await this.axios.request({
      url: request.path,
      headers: Api.headers(request),
      params: request.qp,
      data: request.body as Req,
    });
    if (response.status === 404) return undefined as Res;
    return response.data as Res;
  }

  /* eslint-disable */
  private static headers(request: Request<unknown>): Record<string, string> {
    const result: Record<string, string> = {
      'Accept': 'application/json',
    };
    if (request.body) result['Content-Type'] = 'application/json';
    return result;
  }

  /* eslint-enable */
}

export const apiState = selector<Api>({
  key: 'api/api',
  get: () => new Api(),
});
