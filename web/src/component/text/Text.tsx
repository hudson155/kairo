import classNames from 'classnames';
import React, { ReactNode } from 'react';
import styles from './Text.module.scss';

export type TextSize = 'small' | 'normal';

type TextWeight = 'normal' | 'bold';

interface Props {
  className?: string;

  /**
   * Don't provide this prop unless you need to override the text size.
   */
  size?: TextSize;

  /**
   * Don't provide this prop unless you need to override the text weight.
   */
  weight?: TextWeight;

  children: ReactNode;
}

/**
 * Use this to adjust how text looks without adding additional spacing.
 * For document text that needs proper spacing, use [Paragraph] instead.
 */
const Text: React.FC<Props> = ({
  className = undefined,
  size = undefined,
  weight = undefined,
  children,
}) => {
  return (
    <span className={classNames(sizeClassName(size), weightClassName(weight), className)}>
      {children}
    </span>
  );
};

export default Text;

export const sizeClassName = (size: TextSize | undefined): string | undefined => {
  switch (size) {
  case undefined:
    return undefined;
  case `small`:
    return styles.sizeSmall;
  case `normal`:
    return styles.sizeNormal;
  default:
    throw new Error(`Unsupported font size: ${size}.`);
  }
};

export const weightClassName = (weight: TextWeight | undefined): string | undefined => {
  switch (weight) {
  case undefined:
    return undefined;
  case `normal`:
    return styles.weightNormal;
  case `bold`:
    return styles.weightBold;
  default:
    throw new Error(`Unsupported font weight: ${weight}.`);
  }
};
