import { AuthSetJwtAction } from './AuthAction';
import { AnyAction } from 'redux';
import { ThunkAction } from 'redux-thunk';
import State from '../../state';
import UserActions from '../user/UserActions';
import jsonwebtoken from 'jsonwebtoken';

function setJwt(jwt: string): AuthSetJwtAction {
  return { type: 'AUTH__SET_JWT', jwt };
}

const AuthActions = {
  ensureSetJwt(getJwt: () => Promise<string>): ThunkAction<Promise<void>, State, null, AnyAction> {
    return async (dispatch, getState): Promise<void> => {
      if (getState().auth.loadingStatus === 'NOT_LOADED_OR_LOADING') {
        getJwt().then((jwt: string) => {
          dispatch(setJwt(jwt));
          const decodedJwt = jsonwebtoken.decode(jwt) as { [key: string]: any };
          const jwtUserClaim = JSON.parse(decodedJwt['https://limberapp.io/user']);
          dispatch(UserActions.setJwtUser({
            id: jwtUserClaim.id,
            firstName: jwtUserClaim.firstName,
            lastName: jwtUserClaim.lastName,
          }));
        });
      }
    };
  },
};
export default AuthActions;
