import classNames from 'classnames';
import styles from 'component/text/Text.module.scss';
import React, { ReactNode } from 'react';

export type Size = 'small' | 'normal' | 'large';

type Weight = 'light' | 'normal' | 'bold';

interface Props {
  className?: string;
  size?: Size; // Don't provide this prop unless you need to override the text size.
  weight?: Weight; // Don't provide this prop unless you need to override the text weight.
  children: ReactNode;
}

/**
 * Use this to adjust how text looks without adding additional spacing.
 * For document text that needs proper spacing, use {@link Paragraph} instead.
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

export const sizeClassName = (size: Size | undefined): string | undefined => {
  switch (size) {
  case undefined:
    return undefined;
  case 'small':
    return styles.sizeSmall;
  case 'normal':
    return styles.sizeNormal;
  case 'large':
    return styles.sizeLarge;
  }
};

export const weightClassName = (weight: Weight | undefined): string | undefined => {
  switch (weight) {
  case undefined:
    return undefined;
  case 'light':
    return styles.weightLight;
  case 'normal':
    return styles.weightNormal;
  case 'bold':
    return styles.weightBold;
  }
};
