import UserModel from '../../models/UserModel';
import { SetUserAction } from './UserAction';
import { ThunkDispatch } from 'redux-thunk';
import { AnyAction } from 'redux';
import { useAuth0 } from '../../react-auth0-wrapper';
import jsonwebtoken from 'jsonwebtoken';

function applyJwt() {
  return async (dispatch: ThunkDispatch<{}, {}, AnyAction>): Promise<void> => {
    try {
      const jwtAsString = await useAuth0().getTokenSilently();
      const jwt = jsonwebtoken.decode(jwtAsString);
      const user = JSON.parse(jwt['https://limberapp.io/user']);
      // TODO: After parsing JSON, ensure types are correct.
      dispatch(set(user));
    } catch (e) {
      // TODO
    }
  };
}

function set(user: UserModel): SetUserAction {
  return { type: 'USER_SET', user };
}

const UserActions = {
  applyJwt,
  set,
};
export default UserActions;
