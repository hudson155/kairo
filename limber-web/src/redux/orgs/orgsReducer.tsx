import OrgsState from './OrgsState';
import OrgsAction, { OrgsSetAllAction } from './OrgsAction';

const defaultState: OrgsState = { loadingStatus: 'NOT_LOADED_OR_LOADING', orgs: new Map() };

const orgsReducer = (state: OrgsState = defaultState, abstractAction: OrgsAction): OrgsState => {
  switch (abstractAction.type) {
    case 'ORGS_SET_ALL': {
      const action = abstractAction as OrgsSetAllAction;
      return { ...state, loadingStatus: 'LOADED', orgs: action.orgs };
    }
    case 'ORGS_START_LOADING': {
      return { ...state, loadingStatus: 'LOADING' };
    }
    default:
      return state;
  }
};
export default orgsReducer;
