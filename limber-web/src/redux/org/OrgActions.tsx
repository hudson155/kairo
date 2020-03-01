import Api from '../../api/Api';
import { TA } from '../../index';
import OrgModel from '../../models/org/OrgModel';
import { assertLoaded } from '../util/LoadableState';
import { OrgSetOrgAction, OrgSetOrgErrorAction, OrgStartLoadingOrgAction } from './OrgAction';

function startLoadingOrg(): OrgStartLoadingOrgAction {
  return { type: 'ORG__START_LOADING_ORG' };
}

function setOrg(org: OrgModel): OrgSetOrgAction {
  return { type: 'ORG__SET_ORG', org };
}

function setOrgError(message: string): OrgSetOrgErrorAction {
  return { type: 'ORG__SET_ORG_ERROR', message };
}

const OrgActions = {
  ensureLoaded(): TA {
    return async (dispatch, getState): Promise<void> => {
      if (getState().org.loadingStatus !== 'INITIAL') return;
      dispatch(startLoadingOrg());
      const orgId = assertLoaded(getState().auth).org.id;
      const response = await Api.orgs.getOrg(orgId);
      if (response === undefined) {
        setOrgError(`The org with ID ${orgId} must exist, but it does not.`);
        return;
      }
      dispatch(setOrg(response));
    };
  },
};
export default OrgActions;
