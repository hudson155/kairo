import AuthState from './AuthState';
import AuthAction, { AuthSetJwtAction } from './AuthAction';
import jsonwebtoken from 'jsonwebtoken';

const defaultState: AuthState = { loadingStatus: 'NOT_LOADED_OR_LOADING' };

const authReducer = (state: AuthState = defaultState, abstractAction: AuthAction): AuthState => {
  switch (abstractAction.type) {
    case 'AUTH__SET_JWT': {
      const action = abstractAction as AuthSetJwtAction;
      const decodedJwt = jsonwebtoken.decode(action.jwt) as { [key: string]: any };
      const userClaim = JSON.parse(decodedJwt['https://limberapp.io/user']);
      return {
        ...state,
        loadingStatus: 'LOADED',
        jwt: action.jwt,
        user: {
          id: userClaim.id,
          firstName: userClaim.firstName,
          lastName: userClaim.lastName,
        },
      };
    }
    default:
      return state;
  }
};
export default authReducer;
