import Collapsible from 'component/collapsible/Collapsible';
import styles from 'component/input/errorMessage/InputErrorMessage.module.scss';
import React, { ReactNode } from 'react';

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
