import UserState from './UserState';
import UserAction, { SetUserAction } from './UserAction';

const defaultState: UserState = { loadingStatus: 'NOT_LOADED_OR_LOADING' };

const userReducer = (state: UserState = defaultState, abstractAction: UserAction): UserState => {
  switch (abstractAction.type) {
    case 'USER__SET': {
      const action = abstractAction as SetUserAction;
      return { ...state, user: action.user };
    }
    default:
      return state;
  }
};
export default userReducer;
