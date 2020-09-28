export class TenantRepComplete {
  public readonly orgGuid: string;
  public readonly name: string;
  public readonly auth0ClientId: string;

  constructor(orgGuid: string, name: string, auth0ClientId: string) {
    this.orgGuid = orgGuid;
    this.name = name;
    this.auth0ClientId = auth0ClientId;
  }
}
