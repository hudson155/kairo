import Api from '../../api/Api';
import { TA } from '../../index';
import OrgModel from '../../models/org/OrgModel';
import { LoadingStatus } from '../util/LoadingStatus';
import { OrgSetOrgAction, OrgStartLoadingOrgAction } from './OrgAction';

function startLoadingOrg(): OrgStartLoadingOrgAction {
  return { type: 'ORG__START_LOADING_ORG' };
}

function setOrg(org: OrgModel): OrgSetOrgAction {
  return { type: 'ORG__SET_ORG', org };
}

function assertLoaded<T>(loadingStatus: LoadingStatus, state?: T): T {
  if (loadingStatus !== 'LOADED') throw Error('TODO');
  return state!!
}

const OrgActions = {
  ensureLoaded(): TA {
    return async (dispatch, getState): Promise<void> => {
      if (getState().org.loadingStatus !== 'NOT_LOADED_OR_LOADING') return;
      dispatch(startLoadingOrg());
      const orgId = assertLoaded(getState().auth.loadingStatus, getState().auth.model).org.id;
      const response = (await Api.orgs.getOrg(orgId))!!; // TODO: No double bang
      console.log(response);
      dispatch(setOrg(response));
    };
  },
};
export default OrgActions;
