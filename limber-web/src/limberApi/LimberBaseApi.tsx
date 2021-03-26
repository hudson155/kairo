import axios, { AxiosInstance } from 'axios';
import env from '../env';

type HttpMethod = 'DELETE' | 'GET' | 'PATCH' | 'POST' | 'PUT';

export default class LimberBaseApi {
  private readonly client: AxiosInstance = axios.create({ baseURL: env.LIMBER_API_BASE_URL });
  private readonly getJwt: () => Promise<string | undefined>;
  private readonly additionalApiLatency: number;

  constructor(getJwt: () => Promise<string | undefined>, additionalApiLatency: number) {
    this.getJwt = getJwt;
    this.additionalApiLatency = additionalApiLatency;
  }

  request<T>(method: HttpMethod, url: string, params: any = {}): Promise<T | undefined> {
    return new Promise<T | undefined>((resolve, reject) => {
      setTimeout(async () => {
        try {
          const headers = await this.headers();
          const response = await this.client.request<T>({ method, url, params, headers });
          resolve(response.data);
        } catch (e) {
          if (e.response?.status === 404) {
            resolve(undefined);
            return;
          }
          reject(e);
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
