import UserModel from '../../models/user/UserModel';
import LoadableState from '../util/LoadableState';
import UserAction, { UserSetUserAction, UserSetUserErrorAction } from './UserAction';

const defaultState: LoadableState<UserModel> = { loadingStatus: 'INITIAL' };

const userReducer = (state: LoadableState<UserModel> = defaultState, abstractAction: UserAction): LoadableState<UserModel> => {
  switch (abstractAction.type) {
    case 'USER__START_LOADING_USER': {
      return { ...state, loadingStatus: 'LOADING' };
    }
    case 'USER__SET_USER': {
      const action = abstractAction as UserSetUserAction;
      return { ...state, loadingStatus: 'LOADED', model: action.user };
    }
    case 'USER__SET_USER_ERROR': {
      const action = abstractAction as UserSetUserErrorAction;
      return { ...state, loadingStatus: 'ERROR', errorMessage: action.message };
    }
    default:
      return state;
  }
};

export default userReducer;
