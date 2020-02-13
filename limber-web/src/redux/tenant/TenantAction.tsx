import TenantModel from '../../models/tenant/TenantModel';

export default interface TenantAction {
  type: 'TENANT__START_LOADING_TENANT' | 'TENANT__SET_TENANT';
}

export interface TenantStartLoadingTenantAction extends TenantAction {
  type: 'TENANT__START_LOADING_TENANT';
}

export interface TenantSetTenantAction extends TenantAction {
  type: 'TENANT__SET_TENANT';
  tenant: TenantModel;
}
