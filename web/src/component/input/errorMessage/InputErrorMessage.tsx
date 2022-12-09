import Collapsible from 'component/collapsible/Collapsible';
import React, { ReactNode } from 'react';
import styles from './InputErrorMessage.module.scss';

interface Props {
  children: ReactNode;
}

const InputErrorMessage: React.FC<Props> = ({ children }) => {
  return (
    <Collapsible className={styles.outer} isOpen={Boolean(children)}>
      <div className={styles.inner}>
        {children}
      </div>
    </Collapsible>
  );
};

export default InputErrorMessage;
