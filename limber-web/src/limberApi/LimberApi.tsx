import LimberBaseApi from './LimberBaseApi';
import OrgApi from './OrgApi';
import TenantApi from './TenantApi';
import UserApi from './UserApi';

/**
 * Enables interaction with the Limber backend. In order to avoid an excessively large file, the API
 * interaction is broken down into delegate APIs that all use a common shared base.
 */
export default class LimberApi {
  readonly org: OrgApi;
  readonly tenant: TenantApi;
  readonly user: UserApi;

  constructor(getJwt: () => Promise<string | undefined>, additionalApiLatency: number) {
    const api = new LimberBaseApi(getJwt, additionalApiLatency);
    this.org = new OrgApi(api);
    this.tenant = new TenantApi(api);
    this.user = new UserApi(api);
  }
}
