import OrgModel from '../../models/OrgModel';

export default interface OrgsAction {
  type: 'OrgsSetAll' | 'OrgsStartLoading';
}

export interface OrgsSetAllAction extends OrgsAction {
  type: 'OrgsSetAll';
  orgs: Map<string, OrgModel>;
}

export interface OrgsStartLoadingAction extends OrgsAction {
  type: 'OrgsStartLoading';
}
