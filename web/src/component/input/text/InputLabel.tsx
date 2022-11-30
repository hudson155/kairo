import Text from 'component/text/Text';
import React, { ReactNode } from 'react';
import styles from './InputLabel.module.scss';

interface Props {
  label: string;
  children: ReactNode;
}

const InputLabel: React.FC<Props> = ({ label, children }) => {
  return (
    <label className={styles.container}>
      <Text className={styles.label}>{label}</Text>
      {children}
    </label>
  );
};

export default InputLabel;
