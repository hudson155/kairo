import AuthState from './AuthState';
import AuthAction, { AuthSetJwtAction } from './AuthAction';
import jsonwebtoken from 'jsonwebtoken';

const defaultState: AuthState = { loadingStatus: 'NOT_LOADED_OR_LOADING' };

const authReducer = (state: AuthState = defaultState, abstractAction: AuthAction): AuthState => {
  switch (abstractAction.type) {
    case 'AUTH__SET_JWT': {
      const action = abstractAction as AuthSetJwtAction;
      const decodedJwt = jsonwebtoken.decode(action.jwt) as { [key: string]: any };
      const orgClaim = JSON.parse(decodedJwt['https://limberapp.io/org']);
      const userClaim = JSON.parse(decodedJwt['https://limberapp.io/user']);
      const newState: AuthState = { ...state, loadingStatus: 'LOADED', jwt: action.jwt };
      if (orgClaim !== null) newState.org = { id: orgClaim.id, name: orgClaim.name };
      newState.user = { id: userClaim.id, firstName: userClaim.firstName, lastName: userClaim.lastName };
      return newState;
    }
    default:
      return state;
  }
};
export default authReducer;
