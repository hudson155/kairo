import UserAction, { UserSetUserAction } from './UserAction';
import UserState from './UserState';

const defaultState: UserState = { loadingStatus: 'NOT_LOADED_OR_LOADING' };

const userReducer = (state: UserState = defaultState, abstractAction: UserAction): UserState => {
  switch (abstractAction.type) {
    case 'USER__START_LOADING_USER': {
      const action = abstractAction as UserSetUserAction;
      return { ...state, loadingStatus: 'LOADING' };
    }
    case 'USER__SET_USER': {
      const action = abstractAction as UserSetUserAction;
      return { ...state, user: action.user };
    }
    default:
      return state;
  }
};
export default userReducer;
