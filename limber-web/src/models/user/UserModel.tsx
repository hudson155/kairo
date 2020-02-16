export default interface UserModel {
  id: string;
  orgId: string;
  firstName: string;
  lastName: string;
  emailAddress: string;
  profilePhotoUrl?: string;
}
