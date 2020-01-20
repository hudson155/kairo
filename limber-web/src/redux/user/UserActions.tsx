import UserModel from '../../models/user/UserModel';
import { UserSetJwtUserAction, UserSetUserAction, UserStartLoadingUserAction } from './UserAction';
import JwtUserModel from '../../models/user/JwtUserModel';
import { ThunkAction } from 'redux-thunk';
import State from '../../state';
import { AnyAction } from 'redux';
import jsonwebtoken from 'jsonwebtoken';
import Api from '../../api/Api';

function startLoadingUser(): UserStartLoadingUserAction {
  return { type: 'USER__START_LOADING_USER' };
}

function setJwtUser(jwtUser: JwtUserModel): UserSetJwtUserAction {
  return { type: 'USER__SET_JWT_USER', jwtUser };
}

function setUser(user: UserModel): UserSetUserAction {
  return { type: 'USER__SET_USER', user };
}

const UserActions = {
  setJwtUser,
  ensureLoaded(): ThunkAction<void, State, null, AnyAction> {
    return async (dispatch, getState): Promise<void> => {
      if (getState().user.loadingStatus !== 'NOT_LOADED_OR_LOADING') return;
      dispatch(startLoadingUser());
      const userId = getState().user.jwtUser?.id;
      if (userId === undefined) return;
      const response = (await Api.users.getUser(userId))!!; // TODO: No double bang
      console.log(response);
      dispatch(setUser(response));
    };
  },
};
export default UserActions;
