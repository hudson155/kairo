import TenantModel from '../../models/tenant/TenantModel';

export default interface TenantAction {
  type: 'TENANT__START_LOADING_TENANT' | 'TENANT__SET_TENANT' | 'TENANT__SET_TENANT_ERROR';
}

export interface TenantStartLoadingTenantAction extends TenantAction {
  type: 'TENANT__START_LOADING_TENANT';
}

export interface TenantSetTenantAction extends TenantAction {
  type: 'TENANT__SET_TENANT';
  tenant: TenantModel;
}

export interface TenantSetTenantErrorAction extends TenantAction {
  type: 'TENANT__SET_TENANT_ERROR';
  message: string;
}
