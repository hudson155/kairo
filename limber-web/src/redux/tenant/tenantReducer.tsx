import TenantModel from '../../models/tenant/TenantModel';
import LoadableState from '../util/LoadableState';
import TenantAction, { TenantSetTenantAction, TenantSetTenantErrorAction } from './TenantAction';

const defaultState: LoadableState<TenantModel> = { loadingStatus: 'INITIAL' };

const tenantReducer = (state: LoadableState<TenantModel> = defaultState, abstractAction: TenantAction): LoadableState<TenantModel> => {
  switch (abstractAction.type) {
    case 'TENANT__START_LOADING_TENANT': {
      return { ...state, loadingStatus: 'LOADING' };
    }
    case 'TENANT__SET_TENANT': {
      const action = abstractAction as TenantSetTenantAction;
      return { ...state, loadingStatus: 'LOADED', model: action.tenant };
    }
    case 'TENANT__SET_TENANT_ERROR': {
      const action = abstractAction as TenantSetTenantErrorAction;
      return { ...state, loadingStatus: 'ERROR', errorMessage: action.message };
    }
    default:
      return state;
  }
};

export default tenantReducer;
