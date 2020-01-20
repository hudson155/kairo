export default interface AuthAction {
  type: 'AUTH_SET_JWT';
}

export interface AuthSetJwtAction extends AuthAction {
  type: 'AUTH_SET_JWT';
  jwt: string;
}
