import Spinner from 'component/spinner/Spinner';
import React from 'react';
import styles from './ButtonSubmittingOverlay.module.scss';

const ButtonSubmittingOverlay: React.FC = () => {
  return (
    <div className={styles.overlay}>
      <Spinner />
    </div>
  );
};

export default ButtonSubmittingOverlay;
