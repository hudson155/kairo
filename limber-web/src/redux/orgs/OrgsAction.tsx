import OrgModel from '../../models/orgs/OrgModel';

export default interface OrgsAction {
  type: 'ORGS__START_LOADING' | 'ORGS__SET_ALL';
}

export interface OrgsStartLoadingAction extends OrgsAction {
  type: 'ORGS__START_LOADING';
}

export interface OrgsSetAllAction extends OrgsAction {
  type: 'ORGS__SET_ALL';
  orgs: Map<string, OrgModel>;
}
