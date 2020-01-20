import OrgModel from '../../models/org/OrgModel';
import { OrgSetOrgAction, OrgStartLoadingOrgAction } from './OrgAction';
import { ThunkAction } from 'redux-thunk';
import State from '../../state';
import { AnyAction } from 'redux';
import Api from '../../api/Api';

function startLoadingOrg(): OrgStartLoadingOrgAction {
  return { type: 'ORG__START_LOADING_ORG' };
}

function setOrg(org: OrgModel): OrgSetOrgAction {
  return { type: 'ORG__SET_ORG', org };
}

const OrgActions = {
  ensureLoaded(): ThunkAction<void, State, null, AnyAction> {
    return async (dispatch, getState): Promise<void> => {
      if (getState().org.loadingStatus !== 'NOT_LOADED_OR_LOADING') return;
      dispatch(startLoadingOrg());
      const orgId = getState().auth.org?.id;
      if (orgId === undefined) return;
      const response = (await Api.orgs.getOrg(orgId))!!; // TODO: No double bang
      console.log(response);
      dispatch(setOrg(response));
    };
  },
};
export default OrgActions;
