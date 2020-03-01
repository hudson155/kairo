import TenantAction, { TenantSetTenantAction } from './TenantAction';
import TenantState from './TenantState';

const defaultState: TenantState = { loadingStatus: 'NOT_LOADED_OR_LOADING' };

const tenantReducer = (state: TenantState = defaultState, abstractAction: TenantAction): TenantState => {
  switch (abstractAction.type) {
    case 'TENANT__START_LOADING_TENANT': {
      return { ...state, loadingStatus: 'LOADING' };
    }
    case 'TENANT__SET_TENANT': {
      const action = abstractAction as TenantSetTenantAction;
      return { ...state, loadingStatus: 'LOADED', tenant: action.tenant };
    }
    default:
      return state;
  }
};
export default tenantReducer;
