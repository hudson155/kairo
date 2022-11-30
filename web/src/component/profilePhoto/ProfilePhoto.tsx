import ProfilePhotoDelegate from 'component/profilePhoto/ProfilePhotoDelegate';
import React from 'react';
import { useRecoilValueLoadable } from 'recoil';
import profilePhotoUrlState from 'state/auth/profilePhotoUrl';

/**
 * Displays the authenticated user's profile photo.
 * The profile photo should always exist, since Auth0 generates one automatically.
 * However, if it doesn't exist for some reason, an icon will be displayed instead.
 */
const ProfilePhoto: React.FC = () => {
  const profilePhotoUrl = useRecoilValueLoadable(profilePhotoUrlState).valueMaybe();
  return <ProfilePhotoDelegate url={profilePhotoUrl} />;
};

export default ProfilePhoto;
