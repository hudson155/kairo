import TenantModel from '../../models/tenant/TenantModel';
import { LoadingStatus } from '../util/LoadingStatus';

export default interface TenantState {
  loadingStatus: LoadingStatus;
  tenant?: TenantModel;
}
