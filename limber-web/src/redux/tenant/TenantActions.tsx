import Api from '../../api/Api';
import { rootDomain, TA } from '../../index';
import TenantModel from '../../models/tenant/TenantModel';
import { TenantSetTenantAction, TenantSetTenantErrorAction, TenantStartLoadingTenantAction } from './TenantAction';

function startLoadingTenant(): TenantStartLoadingTenantAction {
  return { type: 'TENANT__START_LOADING_TENANT' };
}

function setTenant(tenant: TenantModel): TenantSetTenantAction {
  return { type: 'TENANT__SET_TENANT', tenant };
}

function setTenantError(message: string): TenantSetTenantErrorAction {
  return { type: 'TENANT__SET_TENANT_ERROR', message };
}

const TenantActions = {
  ensureLoaded(): TA {
    return async (dispatch, getState): Promise<void> => {
      if (getState().tenant.loadingStatus !== 'INITIAL') return;
      dispatch(startLoadingTenant());
      const response = await Api.tenants.getTenant(rootDomain);
      if (response === undefined) {
        setTenantError(`The tenant with domain ${rootDomain} must exist, but it does not.`);
        return;
      }
      dispatch(setTenant(response));
    };
  },
};

export default TenantActions;
