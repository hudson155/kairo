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
    borderRadius: theme.size.borderRadius,
  }),
  placeholder: (theme: Theme): CSSObject => ({
    position: 'absolute',
    top: 0,
    left: 0,
    height: '100%',
    width: '100%',
    backgroundColor: theme.color.app.background.placeholderElement,
    border: `${theme.size.$1} dashed ${theme.color.app.text.normal}`,
    borderRadius: theme.size.borderRadius,
  }),
  image: (theme: Theme): CSSObject => ({
    position: 'absolute',
    top: 0,
    left: 0,
    height: '100%',
    width: '100%',
    borderRadius: theme.size.borderRadius,
  }),
};

interface Props {
  placeholder: string;
  url?: string;
}

/**
 * Shows the profile photo at the given URL. Assumed to be square. If no URL is provided, shows the
 * placeholder text. The placeholder text is intended to be initials, to a maximum of 2 letters. It
 * gets shown in uppercase, and if the placeholder is more than 2 letters it'll only show the first
 * and last.
 */
const ProfilePhoto: React.FC<Props> = ({ placeholder, url }) => {
  const croppedPlaceholder = cropPlaceholder(placeholder);
  return (
    <div css={styles.container}>
      <div css={styles.placeholder}>{croppedPlaceholder}</div>
      {url && <img src={url} css={styles.image} />}
    </div>
  );
};

export default ProfilePhoto;

function cropPlaceholder(placeholder: string): string {
  let result = '';
  if (placeholder.length > 0) result += placeholder[0];
  if (placeholder.length > 1) result += placeholder[placeholder.length - 1];
  return result.toUpperCase();
}
