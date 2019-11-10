import LoadingStatus from '../util/LoadingStatus';
import OrgModel from '../../models/OrgModel';

export default interface OrgsState {
  loadingStatus: LoadingStatus;
  orgs: Map<string, OrgModel>;
}
