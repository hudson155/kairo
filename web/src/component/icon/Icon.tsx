import classNames from 'classnames';
import React from 'react';
import styles from './Icon.module.scss';

export type Size = 'small' | 'medium' | 'large' | 'extra-large';

export type Space = 'none' | 'before' | 'after' | 'both';

interface BaseProps {
  className?: string;

  /**
   * The name must match one of the names from https://fonts.google.com/icons.
   * It is not type checked.
   */
  name: string;

  size?: Size;

  space?: Space;
}

interface SmallProps extends BaseProps {
  size: 'small';
}

interface NonSmallProps extends BaseProps {
  size?: Exclude<Size, 'small'>;
  space?: undefined;
}

type Props = SmallProps | NonSmallProps;

/**
 * An icon, backed by Google Fonts' Material Icons: https://fonts.google.com/icons.
 * Only small icons may have [space] defined, which is why the types above are complex.
 */
const Icon: React.FC<Props> = ({
  className = undefined,
  name,
  size = 'medium',
  space = 'none',
}) => {
  return (
    <span className={classNames('material-icons', sizeClassName(size), spaceClassName(space), className)}>
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

export const spaceClassName = (space: Space): string[] => {
  switch (space) {
  case 'none':
    return [];
  case 'before':
    return [styles.before];
  case 'after':
    return [styles.after];
  case 'both':
    return [styles.before, styles.after];
  default:
    throw new Error(`Unsupported space: ${space}.`);
  }
};
