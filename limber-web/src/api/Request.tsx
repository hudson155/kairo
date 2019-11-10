import { store } from '../index';

export abstract class Request<T> {
  path: string;
  queryParams?: Map<string, string>;

  protected constructor(path: string, queryParams?: Map<string, string>) {
    this.path = path;
    this.queryParams = queryParams;
  }

  async request(): Promise<T> {
    let url = process.env['REACT_APP_API_URL'] as string;
    url += this.path;
    if (this.queryParams?.size) {
      const queryParams: string[] = [];
      this.queryParams.forEach((key, value) => {
        queryParams.push(`${encodeURIComponent(value)}=${encodeURIComponent(key)}`);
      });
      url += `?${queryParams.join('&')}`;
    }

    const headers: { [key: string]: string } = {};
    const jwt = store.getState().auth.jwt;
    if (jwt) {
      headers['Authorization'] = `Bearer ${jwt}`;
    }
    const response: Response = await fetch(url, { headers });
    return await response.json();
  }
}
