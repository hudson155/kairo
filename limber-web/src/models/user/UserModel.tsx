export default interface UserModel {
  id: string;
  created: Date;
  firstName: string;
  lastName: string;
  emailAddress: string;
  profilePhotoUrl?: string;
}
