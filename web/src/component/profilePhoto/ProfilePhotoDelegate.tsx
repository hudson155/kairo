import Icon from 'component/icon/Icon';
import styles from 'component/profilePhoto/ProfilePhoto.module.scss';
import React from 'react';

interface Props {
  url: string | undefined;
}

const ProfilePhotoDelegate: React.FC<Props> = ({ url }) => {
  if (!url) return <Icon name="account_circle" />;
  return <img alt="" className={styles.image} src={url} />;
};

export default ProfilePhotoDelegate;
