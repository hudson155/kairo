import { TA } from '../../index';
import { AuthSetJwtAction } from './AuthAction';

function setJwt(jwt: string): AuthSetJwtAction {
  return { type: 'AUTH__SET_JWT', jwt };
}

const AuthActions = {
  ensureJwtIsSet(getJwt: () => Promise<string>): TA {
    return async (dispatch, getState): Promise<void> => {
      if (getState().auth.loadingStatus !== 'INITIAL') return;
      const jwt = await getJwt();
      dispatch(setJwt(jwt));
    };
  },
};
export default AuthActions;
