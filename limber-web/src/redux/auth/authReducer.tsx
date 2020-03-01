import AuthState from './AuthState';
import AuthAction, { AuthSetJwtAction } from './AuthAction';
import jsonwebtoken from 'jsonwebtoken';

const defaultState: AuthState = { loadingStatus: 'NOT_LOADED_OR_LOADING' };

const authReducer = (state: AuthState = defaultState, abstractAction: AuthAction): AuthState => {
  switch (abstractAction.type) {
    case 'AUTH__SET_JWT': {
      const action = abstractAction as AuthSetJwtAction;
      console.log(`JWT: ${action.jwt}`);
      const decodedJwt = jsonwebtoken.decode(action.jwt) as { [key: string]: any };
      const orgClaim = JSON.parse(decodedJwt['https://limberapp.io/org']);
      const userClaim = JSON.parse(decodedJwt['https://limberapp.io/user']);
      return {
        ...state,
        loadingStatus: 'LOADED',
        auth: {
          jwt: action.jwt,
          org: { id: orgClaim.id, name: orgClaim.name },
          user: { id: userClaim.id, firstName: userClaim.firstName, lastName: userClaim.lastName },
        },
      };
    }
    default:
      return state;
  }
};
export default authReducer;
