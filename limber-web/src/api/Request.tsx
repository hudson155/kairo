export abstract class Request<T> {
  path: string;

  protected constructor(path: string) {
    this.path = path;
  }

  async request(): Promise<T> {
    const response: Response = await fetch(process.env['REACT_APP_API_URL'] + this.path);
    return await response.json();
  }
}
