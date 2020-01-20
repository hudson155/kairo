import OrgModel from '../../models/org/OrgModel';
import { LoadingStatus } from '../util/LoadingStatus';

export default interface OrgState {
  loadingStatus: LoadingStatus;
  org?: OrgModel;
}
