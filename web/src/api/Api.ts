import HttpError from 'api/HttpError';
import ValidationErrorsError, { ValidationError } from 'api/ValidationErrorsError';
import axios, { Axios } from 'axios';
import env from 'env';
import { selectorFamily } from 'recoil';
import auth0ClientState from 'state/auth/auth0Client';

export interface Request<Req> {
  method: 'GET' | 'PATCH';
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
    validateStatus: undefined, // Never throws [AxiosError].
  });

  private readonly getJwt: (() => Promise<string | undefined>);

  constructor(getJwt: () => Promise<string | undefined>) {
    this.getJwt = getJwt;
  }

  /* eslint-disable @typescript-eslint/no-unsafe-member-access */
  async request<Res, Req = null>(request: Request<Req>): Promise<Res> {
    const response = await this.axios.request({
      url: request.path,
      method: request.method,
      headers: await this.headers(request),
      params: request.qp,
      data: request.body as Req,
    });
    if (response.status >= 200 && response.status <= 299) {
      return response.data as Res;
    }
    // The body parameters accessed here correspond with errors from Limber's backend.
    if (response.status === 404 && response.data.message === 'Not found.') {
      return null as Res;
    }
    if (response.status === 400 && response.data.message === 'Validation errors.') {
      throw new ValidationErrorsError(response.data.errors as ValidationError[]);
    }
    throw new HttpError(response);
  }

  /* eslint-enable */

  /* eslint-disable @typescript-eslint/dot-notation, @typescript-eslint/naming-convention, quote-props */
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
      const auth0 = get(auth0ClientState);
      getJwt = () => auth0.getTokenSilently();
    }
    return new Api(getJwt);
  },
});
