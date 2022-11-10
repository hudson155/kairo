import classNames from 'classnames';
import React from 'react';
import styles from './Icon.module.scss';

export type Size = 'small' | 'medium' | 'large' | 'extraLarge';

interface Props {
  /**
   * The name must match one of the names from https://fonts.google.com/icons.
   * It is not type checked.
   */
  name: string;
  size?: Size;
}

/**
 * An icon, backed by Google Fonts' Material Icons: https://fonts.google.com/icons.
 */
const Icon: React.FC<Props> = ({ name, size = `medium` }) => (
  <span className={classNames(`material-icons`, styles[size])}>
    {name}
  </span>
);

export default Icon;
