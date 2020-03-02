import JwtOrgModel from './JwtOrgModel';
import JwtUserModel from './JwtUserModel';

export default interface AuthModel {
  jwt: string;
  org: JwtOrgModel;
  user: JwtUserModel;
}
