import UserModel from '../../models/user/UserModel';
import LoadableState from '../util/LoadableState';
import UserAction, { UserSetUserAction } from './UserAction';

const defaultState: LoadableState<UserModel> = { loadingStatus: 'NOT_LOADED_OR_LOADING' };

const userReducer = (state: LoadableState<UserModel> = defaultState, abstractAction: UserAction): LoadableState<UserModel> => {
  switch (abstractAction.type) {
    case 'USER__START_LOADING_USER': {
      const action = abstractAction as UserSetUserAction;
      return { ...state, loadingStatus: 'LOADING' };
    }
    case 'USER__SET_USER': {
      const action = abstractAction as UserSetUserAction;
      return { ...state, model: action.user };
    }
    default:
      return state;
  }
};
export default userReducer;
