import OrgModel from '../../models/org/OrgModel';
import Request from '../Request';

class GetOrgRequest extends Request<OrgModel | undefined> {
  constructor(orgId: string) {
    super(`/orgs/${encodeURIComponent(orgId)}`);
  }
}

export default async function getOrg(orgId: string): Promise<OrgModel | undefined> {
  return new GetOrgRequest(orgId).request();
}
