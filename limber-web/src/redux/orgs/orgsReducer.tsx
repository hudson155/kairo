import OrgsState from './OrgsState';
import OrgsAction, { OrgsSetAllAction, OrgsStartLoadingAction } from './OrgsAction';

const defaultState: OrgsState = { loadingStatus: 'NOT_LOADED_OR_LOADING', orgs: new Map() };

const orgsReducer = (state: OrgsState = defaultState, abstractAction: OrgsAction): OrgsState => {
  switch (abstractAction.type) {
    case 'OrgsSetAll': {
      const action = abstractAction as OrgsSetAllAction;
      return { ...state, loadingStatus: 'LOADED', orgs: action.orgs };
    }
    case 'OrgsStartLoading': {
      const action = abstractAction as OrgsStartLoadingAction;
      return { ...state, loadingStatus: 'LOADING' };
    }
    default:
      return state;
  }
};
export default orgsReducer;
