import { ThunkDispatch } from 'redux-thunk';
import { AnyAction } from 'redux';
import OrgModel from '../../models/OrgModel';
import { OrgsSetAllAction, OrgsStartLoadingAction } from './OrgsAction';
import Api from '../../api/Api';

function setAll(orgs: Map<string, OrgModel>): OrgsSetAllAction {
  return { type: 'OrgsSetAll', orgs };
}

function startLoading(): OrgsStartLoadingAction {
  return { type: 'OrgsStartLoading' };
}

const OrgsActions = {
  setAllByMemberId(memberId: string) {
    return async (dispatch: ThunkDispatch<{}, {}, AnyAction>): Promise<void> => {
      dispatch(startLoading());
      const response = await Api.orgs.getOrgsByMemberId(memberId);
      console.log(response);
      // dispatch(setAll());
    };
  },
};
export default OrgsActions;
