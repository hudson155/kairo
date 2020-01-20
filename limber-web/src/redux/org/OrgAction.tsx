import OrgModel from '../../models/org/OrgModel';

export default interface OrgAction {
  type: 'ORG__START_LOADING_ORG' | 'ORG__SET_ORG';
}

export interface OrgStartLoadingOrgAction extends OrgAction {
  type: 'ORG__START_LOADING_ORG';
}

export interface OrgSetOrgAction extends OrgAction {
  type: 'ORG__SET_ORG';
  org: OrgModel;
}
