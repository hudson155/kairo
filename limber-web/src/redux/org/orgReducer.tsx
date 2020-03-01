import OrgModel from '../../models/org/OrgModel';
import LoadableState from '../util/LoadableState';
import OrgAction, { OrgSetOrgAction } from './OrgAction';

const defaultState: LoadableState<OrgModel> = { loadingStatus: 'NOT_LOADED_OR_LOADING' };

const orgReducer = (state: LoadableState<OrgModel> = defaultState, abstractAction: OrgAction): LoadableState<OrgModel> => {
  switch (abstractAction.type) {
    case 'ORG__START_LOADING_ORG': {
      return { ...state, loadingStatus: 'LOADING' };
    }
    case 'ORG__SET_ORG': {
      const action = abstractAction as OrgSetOrgAction;
      return { ...state, loadingStatus: 'LOADED', model: action.org };
    }
    default:
      return state;
  }
};
export default orgReducer;
