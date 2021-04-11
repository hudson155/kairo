import OrgRep from '../rep/OrgRep';
import LimberBaseApi from './LimberBaseApi';

export default class OrgApi {
  private readonly api: LimberBaseApi;

  constructor(api: LimberBaseApi) {
    this.api = api;
  }

  async get(orgGuid: string): Promise<OrgRep.Complete | undefined> {
    const url = `/orgs/${orgGuid}`;
    return await this.api.request<OrgRep.Complete>('GET', url);
  }
}
