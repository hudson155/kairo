import jsonwebtoken from 'jsonwebtoken';
import AuthModel from '../../models/auth/AuthModel';
import LoadableState from '../util/LoadableState';
import AuthAction, { AuthSetJwtAction } from './AuthAction';

const defaultState: LoadableState<AuthModel> = { loadingStatus: 'INITIAL' };

const authReducer = (state: LoadableState<AuthModel> = defaultState, abstractAction: AuthAction): LoadableState<AuthModel> => {
  switch (abstractAction.type) {
    case 'AUTH__SET_JWT': {
      const action = abstractAction as AuthSetJwtAction;
      const decodedJwt = jsonwebtoken.decode(action.jwt) as { [key: string]: string | number };
      const orgClaim = JSON.parse(decodedJwt['https://limberapp.io/org'] as string);
      const userClaim = JSON.parse(decodedJwt['https://limberapp.io/user'] as string);
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
