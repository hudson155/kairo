import OrgModel from '../../models/OrgModel';
import { Request } from '../Request';

class GetOrgRequest extends Request<OrgModel> {
  constructor(orgId: string) {
    super(`/orgs/${orgId}`);
  }
}

export default async function getOrg(orgId: string): Promise<OrgModel> {
  return new GetOrgRequest(orgId).request();
}
