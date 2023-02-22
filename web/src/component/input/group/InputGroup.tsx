import styles from 'component/input/group/InputGroup.module.scss';
import React, { ReactNode } from 'react';

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
