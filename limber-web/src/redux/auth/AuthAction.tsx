export default interface AuthAction {
  type: 'AUTH__SET_JWT' | 'AUTH__SET_JWT_ERROR';
}

export interface AuthSetJwtAction extends AuthAction {
  type: 'AUTH__SET_JWT';
  jwt: string;
}

export interface AuthSetJwtErrorAction extends AuthAction {
  type: 'AUTH__SET_JWT_ERROR';
  message: string;
}
