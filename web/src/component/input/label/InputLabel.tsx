import styles from 'component/input/label/InputLabel.module.scss';
import Text from 'component/text/Text';
import React, { ReactNode } from 'react';

interface Props {
  label: string;
  children: ReactNode;
}

const InputLabel: React.FC<Props> = ({ label, children }) => {
  return (
    <label>
      <Text className={styles.label}>{label}</Text>
      {children}
    </label>
  );
};

export default InputLabel;
