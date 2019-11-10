import AuthState from './AuthState';
import AuthAction, { AuthSetJwtAction } from './AuthAction';

const defaultState: AuthState = { loadingStatus: 'NOT_LOADED_OR_LOADING' };

const authReducer = (state: AuthState = defaultState, abstractAction: AuthAction): AuthState => {
  switch (abstractAction.type) {
    case 'AuthSetJwt': {
      const action = abstractAction as AuthSetJwtAction;
      return { ...state, loadingStatus: 'LOADED', jwt: action.jwt };
    }
    default:
      return state;
  }
};
export default authReducer;
