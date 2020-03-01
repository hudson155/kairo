import OrgAction, { OrgSetOrgAction } from './OrgAction';
import OrgState from './OrgState';

const defaultState: OrgState = { loadingStatus: 'NOT_LOADED_OR_LOADING' };

const orgReducer = (state: OrgState = defaultState, abstractAction: OrgAction): OrgState => {
  switch (abstractAction.type) {
    case 'ORG__START_LOADING_ORG': {
      return { ...state, loadingStatus: 'LOADING' };
    }
    case 'ORG__SET_ORG': {
      const action = abstractAction as OrgSetOrgAction;
      return { ...state, loadingStatus: 'LOADED', org: action.org };
    }
    default:
      return state;
  }
};
export default orgReducer;
