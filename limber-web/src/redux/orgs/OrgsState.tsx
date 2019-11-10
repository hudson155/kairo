import OrgModel from '../../models/OrgModel';
import { LoadingStatus } from '../util/LoadingStatus';

export default interface OrgsState {
  loadingStatus: LoadingStatus;
  orgs: Map<string, OrgModel>;
}
