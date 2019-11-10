import OrgModel from '../../models/OrgModel';
import { Request } from '../Request';

class GetOrgsByMemberIdRequest extends Request<OrgModel[]> {
  constructor(memberId: string) {
    super(`/orgs`, new Map([['memberId', memberId]]));
  }
}

export default async function getOrgsByMemberId(memberId: string): Promise<OrgModel[]> {
  return new GetOrgsByMemberIdRequest(memberId).request();
}
