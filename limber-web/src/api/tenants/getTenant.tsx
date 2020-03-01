import { Request } from '../Request';
import TenantModel from '../../models/tenant/TenantModel';

class GetTenantRequest extends Request<TenantModel | undefined> {
  constructor(tenantDomain: string) {
    super(`/tenants/${encodeURIComponent(tenantDomain)}`);
  }
}

export default async function getTenant(tenantDomain: string): Promise<TenantModel | undefined> {
  return new GetTenantRequest(tenantDomain).request();
}
