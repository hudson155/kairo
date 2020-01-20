import JwtUserModel from './JwtUserModel';

export default interface UserModel extends JwtUserModel {
  emailAddress?: string;
  profilePhotoUrl?: string;
}
