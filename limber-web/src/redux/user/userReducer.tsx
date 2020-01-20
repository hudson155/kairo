import UserState from './UserState';
import UserAction, { SetJwtUserAction, SetUserAction } from './UserAction';

const defaultState: UserState = { loadingStatus: 'NOT_LOADED_OR_LOADING' };

const userReducer = (state: UserState = defaultState, abstractAction: UserAction): UserState => {
  switch (abstractAction.type) {
    case 'USER__SET_USER': {
      const action = abstractAction as SetUserAction;
      return { ...state, jwtUser: action.user, user: action.user };
    }
    case 'USER__SET_JWT_USER': {
      const action = abstractAction as SetJwtUserAction;
      return { ...state, jwtUser: action.jwtUser };
    }
    default:
      return state;
  }
};
export default userReducer;
