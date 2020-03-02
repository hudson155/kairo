import UserModel from '../../models/user/UserModel';
import Request from '../Request';

class GetUserRequest extends Request<UserModel | undefined> {
  constructor(userId: string) {
    super(`/users/${encodeURIComponent(userId)}`);
  }
}

export default async function getUser(userId: string): Promise<UserModel | undefined> {
  return new GetUserRequest(userId).request();
}
