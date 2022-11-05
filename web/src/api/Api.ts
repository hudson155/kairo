import axios, { Axios } from 'axios';
import env from 'env';
import { selectorFamily } from 'recoil';
import auth0ClientState from 'state/auth/auth0Client';

export interface Request<Req> {
  method: 'GET';
  path: string;
  qp?: URLSearchParams;
  body?: Req;
}

/**
 * This is the base API class for interacting with the backend.
 * It holds the shared logic, but individual endpoints are defined in other files in this folder.
 */
export default class Api {
  private readonly axios: Axios = axios.create({
    baseURL: env.limber.apiBaseUrl, // eslint-disable-line @typescript-eslint/naming-convention
    timeout: 10_000,
    validateStatus: (status) => (status >= 200 && status < 300) || status === 404,
  });

  private readonly getJwt: (() => Promise<string | undefined>);

  constructor(getJwt: () => Promise<string | undefined>) {
    this.getJwt = getJwt;
  }

  async request<Res, Req = undefined>(request: Request<Req>): Promise<Res> {
    const response = await this.axios.request({
      url: request.path,
      method: request.method,
      headers: await this.headers(request),
      params: request.qp,
      data: request.body as Req,
    });
    // Update [validateStatus] above if we need to handle more statuses here.
    if (response.status === 404) return undefined as Res;
    return response.data as Res;
  }

  /* eslint-disable */
  private async headers(request: Request<unknown>): Promise<Record<string, string>> {
    const jwt = await this.getJwt();
    const result: Record<string, string> = {
      'Accept': 'application/json',
    };
    if (jwt) result['Authorization'] = `Bearer ${jwt}`;
    if (request.body) result['Content-Type'] = 'application/json';
    return result;
  }

  /* eslint-enable */
}

export const apiState = selectorFamily<Api, {
  /**
   * Passing [true] will result in an authenticated API (that passes a JWT with each request).
   * This is used for most requests.
   *
   * Passing [false] will result in an unauthenticated API.
   * This is useful when there is no user session.
   */
  authenticated: boolean;
}>({
  key: 'api/api',
  get: ({ authenticated }) => ({ get }) => {
    let getJwt: () => Promise<string | undefined> = () => Promise.resolve(undefined);
    if (authenticated) {
      const auth0Client = get(auth0ClientState);
      getJwt = () => auth0Client.getTokenSilently();
    }
    return new Api(getJwt);
  },
});
