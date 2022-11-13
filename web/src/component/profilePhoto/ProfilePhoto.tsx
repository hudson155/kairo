import Icon from 'component/icon/Icon';
import React from 'react';
import { useRecoilValueLoadable } from 'recoil';
import profilePhotoUrlState from 'state/auth/profilePhotoUrl';
import styles from './ProfilePhoto.module.scss';

/**
 * Displays the authenticated user's profile photo.
 * The profile photo should always exist, since Auth0 generates one automatically.
 * However, if it doesn't exist for some reason, an icon will be displayed instead.
 */
const ProfilePhoto: React.FC = () => {
  const profilePhotoUrl = useRecoilValueLoadable(profilePhotoUrlState).valueMaybe();
  return <Delegate url={profilePhotoUrl} />;
};

export default ProfilePhoto;

export const Delegate: React.FC<{ url: string | undefined }> = ({ url }) => {
  if (!url) return <Icon name="account_circle" />;
  return <img alt="" className={styles.image} src={url} />;
};
