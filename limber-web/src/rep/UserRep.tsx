namespace UserRep {
  export interface Complete {
    readonly guid: string;
    readonly emailAddress: string;
    readonly fullName: string;
    readonly initials: string;
    readonly profilePhotoUrl?: string;
  }
}

export default UserRep;
