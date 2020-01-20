import UserState from './UserState';
import UserAction, { UserSetJwtUserAction, UserSetUserAction } from './UserAction';

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
    case 'USER__SET_JWT_USER': {
      const action = abstractAction as UserSetJwtUserAction;
      return { ...state, jwtUser: action.jwtUser };
    }
    default:
      return state;
  }
};
export default userReducer;
