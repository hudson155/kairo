import Api from '../../api/Api';
import { rootDomain, TA } from '../../index';
import TenantModel from '../../models/tenant/TenantModel';
import { TenantSetTenantAction, TenantStartLoadingTenantAction } from './TenantAction';

function startLoadingTenant(): TenantStartLoadingTenantAction {
  return { type: 'TENANT__START_LOADING_TENANT' };
}

function setTenant(tenant: TenantModel): TenantSetTenantAction {
  return { type: 'TENANT__SET_TENANT', tenant };
}

const TenantActions = {
  ensureLoaded(): TA {
    return async (dispatch, getState): Promise<void> => {
      if (getState().tenant.loadingStatus !== 'NOT_LOADED_OR_LOADING') return;
      dispatch(startLoadingTenant());
      const tenantDomain = rootDomain;
      const response = (await Api.tenants.getTenant(tenantDomain))!!; // TODO: No double bang
      console.log(response);
      dispatch(setTenant(response));
    };
  },
};
export default TenantActions;
