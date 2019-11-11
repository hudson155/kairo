import { AuthSetJwtAction } from './AuthAction';
import { AnyAction } from 'redux';
import { ThunkAction } from 'redux-thunk';
import State from '../../state';

function setJwt(jwt: string): AuthSetJwtAction {
  return { type: 'AuthSetJwt', jwt };
}

const AuthActions = {
  ensureSetJwt(getJwt: () => Promise<string>): ThunkAction<void, State, null, AnyAction> {
    return async (dispatch, getState): Promise<void> => {
      if (getState().auth.loadingStatus === 'NOT_LOADED_OR_LOADING') {
        getJwt().then((jwt: string) => dispatch(setJwt(jwt)));
      }
    };
  },
};
export default AuthActions;
