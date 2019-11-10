import { ThunkDispatch } from 'redux-thunk';
import { AnyAction } from 'redux';
import { AuthSetJwtAction } from './AuthAction';
import OrgsActions from '../orgs/OrgsActions';
import jsonwebtoken from 'jsonwebtoken';

function setJwt(jwt: string) {
  return async (dispatch: ThunkDispatch<{}, {}, AnyAction>): Promise<void> => {
    const decoded = jsonwebtoken.decode(jwt) as { [key: string]: any };
    const orgsClaim = JSON.parse(decoded['https://limberapp.io/orgs'] as string);
    const rolesClaim = JSON.parse(decoded['https://limberapp.io/roles'] as string);
    const userClaim = JSON.parse(decoded['https://limberapp.io/user'] as string);
    const authSetJwtAction: AuthSetJwtAction = { type: 'AuthSetJwt', jwt };
    dispatch(authSetJwtAction);

    dispatch(OrgsActions.setAllByMemberId(userClaim.id));
  };
}

const AuthActions = {
  setJwt,
};
export default AuthActions;
