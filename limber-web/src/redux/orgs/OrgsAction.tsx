import OrgModel from '../../models/orgs/OrgModel';

export default interface OrgsAction {
  type: 'ORGS_SET_ALL' | 'ORGS_START_LOADING';
}

export interface OrgsSetAllAction extends OrgsAction {
  type: 'ORGS_SET_ALL';
  orgs: Map<string, OrgModel>;
}

export interface OrgsStartLoadingAction extends OrgsAction {
  type: 'ORGS_START_LOADING';
}
