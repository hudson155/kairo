import { AnyAction } from 'redux';
import OrgModel from '../../models/orgs/OrgModel';
import { OrgsSetAllAction, OrgsStartLoadingAction } from './OrgsAction';
import Api from '../../api/Api';
import { ThunkAction } from 'redux-thunk';
import State from '../../state';

function setAll(orgs: Map<string, OrgModel>): OrgsSetAllAction {
  return { type: 'ORGS__SET_ALL', orgs };
}

function startLoading(): OrgsStartLoadingAction {
  return { type: 'ORGS__START_LOADING' };
}

const OrgsActions = {
  setAllByMemberId(memberId: string): ThunkAction<void, State, null, AnyAction> {
    return async (dispatch): Promise<void> => {
      dispatch(startLoading());
      const response = await Api.orgs.getOrgsByMemberId(memberId);
      console.log(response);
      // dispatch(setAll());
    };
  },
};
export default OrgsActions;
