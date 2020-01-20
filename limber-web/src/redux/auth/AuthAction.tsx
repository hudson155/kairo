export default interface AuthAction {
  type: 'AUTH__SET_JWT';
}

export interface AuthSetJwtAction extends AuthAction {
  type: 'AUTH__SET_JWT';
  jwt: string;
}
