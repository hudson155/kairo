import Icon from 'component/icon/Icon';
import React from 'react';
import styles from './ProfilePhoto.module.scss';

interface Props {
  url: string | undefined;
}

const ProfilePhotoDelegate: React.FC<Props> = ({ url }) => {
  if (!url) return <Icon name="account_circle" />;
  return <img alt="" className={styles.image} src={url} />;
};

export default ProfilePhotoDelegate;
