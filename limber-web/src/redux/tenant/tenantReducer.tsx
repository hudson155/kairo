import TenantModel from '../../models/tenant/TenantModel';
import LoadableState from '../util/LoadableState';
import TenantAction, { TenantSetTenantAction } from './TenantAction';

const defaultState: LoadableState<TenantModel> = { loadingStatus: 'NOT_LOADED_OR_LOADING' };

const tenantReducer = (state: LoadableState<TenantModel> = defaultState, abstractAction: TenantAction): LoadableState<TenantModel> => {
  switch (abstractAction.type) {
    case 'TENANT__START_LOADING_TENANT': {
      return { ...state, loadingStatus: 'LOADING' };
    }
    case 'TENANT__SET_TENANT': {
      const action = abstractAction as TenantSetTenantAction;
      return { ...state, loadingStatus: 'LOADED', model: action.tenant };
    }
    default:
      return state;
  }
};
export default tenantReducer;
