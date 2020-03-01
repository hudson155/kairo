import jsonwebtoken from 'jsonwebtoken';
import AuthModel from '../../models/auth/AuthModel';
import LoadableState from '../util/LoadableState';
import AuthAction, { AuthSetJwtAction } from './AuthAction';

const defaultState: LoadableState<AuthModel> = { loadingStatus: 'NOT_LOADED_OR_LOADING' };

const authReducer = (state: LoadableState<AuthModel> = defaultState, abstractAction: AuthAction): LoadableState<AuthModel> => {
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
        model: {
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
