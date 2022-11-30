import classNames from 'classnames';
import React from 'react';
import styles from './Icon.module.scss';

export type Size = 'small' | 'medium' | 'large' | 'extraLarge';

interface BaseProps {
  className?: string;

  /**
   * The name must match one of the names from https://fonts.google.com/icons.
   * It is not type checked.
   */
  name: string;

  size?: Size;

  space?: 'none' | 'before' | 'after' | 'both';
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
  size = `medium`,
  space = `none`,
}) => {
  return (
    <span className={classNames(`material-icons`, styles[size], styles[space], className)}>
      {name}
    </span>
  );
};

export default Icon;
