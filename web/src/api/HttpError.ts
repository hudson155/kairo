import { AxiosResponse } from 'axios';

export default class HttpError extends Error {
  readonly response: AxiosResponse;

  constructor(response: AxiosResponse) {
    super(`Request failed with status code ${response.status}.`);
    this.name = 'HttpError';
    this.response = response;
  }
}
