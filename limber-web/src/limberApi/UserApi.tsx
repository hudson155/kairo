import UserRep from '../rep/UserRep';
import LimberBaseApi from './LimberBaseApi';

export default class UserApi {
  private readonly api: LimberBaseApi;

  constructor(api: LimberBaseApi) {
    this.api = api;
  }

  async get(userGuid: string): Promise<UserRep.Complete | undefined> {
    const url = `/users/${userGuid}`;
    return await this.api.request<UserRep.Complete>('GET', url);
  }
}
