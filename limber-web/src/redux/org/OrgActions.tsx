import OrgModel from '../../models/org/OrgModel';
import { OrgSetOrgAction, OrgStartLoadingOrgAction } from './OrgAction';

function startLoadingOrg(): OrgStartLoadingOrgAction {
  return { type: 'ORG__START_LOADING_ORG' };
}

function setOrg(org: OrgModel): OrgSetOrgAction {
  return { type: 'ORG__SET_ORG', org };
}

const OrgActions = {};
export default OrgActions;
