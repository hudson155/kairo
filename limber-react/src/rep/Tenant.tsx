export class TenantRepComplete {
  public readonly orgGuid: string;
  public readonly auth0ClientId: string;

  constructor(orgGuid: string, auth0ClientId: string) {
    this.orgGuid = orgGuid;
    this.auth0ClientId = auth0ClientId;
  }
}
