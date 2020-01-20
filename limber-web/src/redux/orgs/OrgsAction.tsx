import OrgModel from '../../models/orgs/OrgModel';

export default interface OrgsAction {
  type: 'ORGS__SET_ALL' | 'ORGS__START_LOADING';
}

export interface OrgsSetAllAction extends OrgsAction {
  type: 'ORGS__SET_ALL';
  orgs: Map<string, OrgModel>;
}

export interface OrgsStartLoadingAction extends OrgsAction {
  type: 'ORGS__START_LOADING';
}
