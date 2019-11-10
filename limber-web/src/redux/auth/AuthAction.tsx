export default interface AuthAction {
  type: 'AuthSetJwt';
}

export interface AuthSetJwtAction extends AuthAction {
  type: 'AuthSetJwt';
  jwt: string;
}
