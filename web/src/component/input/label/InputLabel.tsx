import classNames from 'classnames';
import Text from 'component/text/Text';
import React, { ReactNode } from 'react';
import styles from './InputLabel.module.scss';

export type InputWidth = 1 | 2;

interface Props {
  label: string;
  width?: InputWidth;
  children: ReactNode;
}

const InputLabel: React.FC<Props> = ({ label, width = 1, children }) => {
  return (
    <label className={classNames(styles.container, widthClassName(width))}>
      <Text className={styles.label}>{label}</Text>
      {children}
    </label>
  );
};

export default InputLabel;

export const widthClassName = (width: InputWidth): string | undefined => {
  switch (width) {
  case 1:
    return undefined;
  case 2:
    return styles.width2;
  default:
    throw new Error(`Unsupported width: ${width}.`);
  }
};
