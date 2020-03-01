import Api from '../../api/Api';
import { TA } from '../../index';
import UserModel from '../../models/user/UserModel';
import { assertLoaded } from '../util/LoadableState';
import { UserSetUserAction, UserSetUserErrorAction, UserStartLoadingUserAction } from './UserAction';

function startLoadingUser(): UserStartLoadingUserAction {
  return { type: 'USER__START_LOADING_USER' };
}

function setUser(user: UserModel): UserSetUserAction {
  return { type: 'USER__SET_USER', user };
}

function setUserError(message: string): UserSetUserErrorAction {
  return { type: 'USER__SET_USER_ERROR', message };
}

const UserActions = {
  ensureLoaded(): TA {
    return async (dispatch, getState): Promise<void> => {
      if (getState().user.loadingStatus !== 'INITIAL') return;
      dispatch(startLoadingUser());
      const userId = assertLoaded(getState().auth).user.id;
      const response = await Api.users.getUser(userId);
      if (response === undefined) {
        setUserError(`The user with ID ${userId} must exist, but it does not.`);
        return;
      }
      dispatch(setUser(response));
    };
  },
};
export default UserActions;
