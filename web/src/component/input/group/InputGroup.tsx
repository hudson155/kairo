import React, { ReactNode } from 'react';
import styles from './InputGroup.module.scss';

interface Props {
  children: ReactNode;
}

const InputGroup: React.FC<Props> = ({ children }) => {
  return (
    <div className={styles.container}>
      {children}
    </div>
  );
};

export default InputGroup;
