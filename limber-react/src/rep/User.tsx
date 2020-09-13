export class UserRepSummary {
  public readonly guid: string;
  public readonly firstName: string | undefined;
  public readonly lastName: string | undefined;
  public readonly profilePhotoUrl: string | undefined;

  constructor(
    guid: string,
    firstName: string | undefined,
    lastName: string | undefined,
    profilePhotoUrl: string,
  ) {
    this.guid = guid;
    this.firstName = firstName;
    this.lastName = lastName;
    this.profilePhotoUrl = profilePhotoUrl;
  }
}

export class UserRepComplete {
  public readonly guid: string;
  public readonly firstName: string | undefined;
  public readonly lastName: string | undefined;
  public readonly fullName: string | undefined;
  public readonly emailAddress: string;
  public readonly profilePhotoUrl: string | undefined;

  constructor(
    guid: string,
    firstName: string | undefined,
    lastName: string | undefined,
    emailAddress: string,
    profilePhotoUrl: string,
  ) {
    this.guid = guid;
    this.firstName = firstName;
    this.lastName = lastName;
    this.fullName = [firstName, lastName].filter(it => it).join(' ') || undefined;
    this.emailAddress = emailAddress;
    this.profilePhotoUrl = profilePhotoUrl;
  }
}
