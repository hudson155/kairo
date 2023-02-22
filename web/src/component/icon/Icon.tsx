import classNames from 'classnames';
import styles from 'component/icon/Icon.module.scss';
import React from 'react';

export type Size = 'small' | 'medium' | 'large' | 'extra-large';

interface Props {
  className?: string;
  name: string; // Must match one of the names from https://fonts.google.com/icons (not type checked).
  size?: Size;
}

/**
 * An icon, backed by Google Fonts' Material Icons: https://fonts.google.com/icons.
 */
const Icon: React.FC<Props> = ({ className = undefined, name, size = 'medium' }) => {
  return (
    <span className={classNames('material-icons', sizeClassName(size), className)}>
      {name}
    </span>
  );
};

export default Icon;

export const sizeClassName = (size: Size): string => {
  switch (size) {
  case 'small':
    return styles.small;
  case 'medium':
    return styles.medium;
  case 'large':
    return styles.large;
  case 'extra-large':
    return styles.extraLarge;
  default:
    throw new Error(`Unsupported size: ${size}.`);
  }
};
