/** @jsxImportSource @emotion/react */

import { CSSObject, Theme } from '@emotion/react';
import React from 'react';

const styles = {
  container: (theme: Theme): CSSObject => ({
    position: 'relative',
    height: theme.size.$32,
    width: theme.size.$32,
    lineHeight: theme.size.$32,
    textAlign: 'center',
  }),
  inner: (theme: Theme): CSSObject => ({
    position: 'absolute',
    boxSizing: 'border-box',
    top: '0',
    left: '0',
    height: '100%',
    width: '100%',
    border: `${theme.size.$1} dashed ${theme.color.grey800}`,
    borderRadius: theme.size.$4,
  }),
  img: (theme: Theme): CSSObject => ({
    position: 'absolute',
    top: '0',
    left: '0',
    height: '100%',
    width: '100%',
    borderRadius: theme.size.$4,
  }),
};

interface ProfilePhotoProps {
  placeholder: string;
  url?: string;
}

const ProfilePhoto: React.FC<ProfilePhotoProps> = ({ placeholder, url }) => {
  const croppedPlaceholder = cropPlaceholder(placeholder);
  return (
    <div css={styles.container}>
      <div css={styles.inner}>{croppedPlaceholder}</div>
      {url && <img src={url} css={styles.img} />}
    </div>
  );
};

export default ProfilePhoto;

function cropPlaceholder(placeholder: string): string {
  let result = '';
  if (placeholder.length > 0) result += placeholder[0];
  if (placeholder.length > 1) result += placeholder[placeholder.length - 1];
  return result;
}
