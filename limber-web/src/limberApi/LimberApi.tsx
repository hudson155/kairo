import LimberBaseApi from './LimberBaseApi';
import TenantApi from './TenantApi';

/**
 * Enables interaction with the Limber backend. In order to avoid an excessively large file, the API
 * interaction is broken down into delegate APIs that all use a common shared base.
 */
export default class LimberApi {
  readonly tenant: TenantApi;

  constructor(getJwt: () => Promise<string | undefined>, additionalApiLatency: number) {
    const api = new LimberBaseApi(getJwt, additionalApiLatency);
    this.tenant = new TenantApi(api);
  }
}
