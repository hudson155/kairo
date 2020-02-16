import UserModel from '../../models/user/UserModel';
import { UserSetUserAction, UserStartLoadingUserAction } from './UserAction';
import { ThunkAction } from 'redux-thunk';
import State from '../../state';
import { AnyAction } from 'redux';
import Api from '../../api/Api';

function startLoadingUser(): UserStartLoadingUserAction {
  return { type: 'USER__START_LOADING_USER' };
}

function setUser(user: UserModel): UserSetUserAction {
  return { type: 'USER__SET_USER', user };
}

const UserActions = {
  ensureLoaded(): ThunkAction<void, State, null, AnyAction> {
    return async (dispatch, getState): Promise<void> => {
      if (getState().user.loadingStatus !== 'NOT_LOADED_OR_LOADING') return;
      dispatch(startLoadingUser());
      const userId = getState().auth.auth!!.user.id;
      const response = (await Api.users.getUser(userId))!!; // TODO: No double bang
      console.log(response);
      dispatch(setUser(response));
    };
  },
};
export default UserActions;
