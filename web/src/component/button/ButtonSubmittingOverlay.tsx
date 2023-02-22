import styles from 'component/button/ButtonSubmittingOverlay.module.scss';
import Spinner from 'component/spinner/Spinner';
import React from 'react';

const ButtonSubmittingOverlay: React.FC = () => {
  return (
    <div className={styles.overlay}>
      <Spinner />
    </div>
  );
};

export default ButtonSubmittingOverlay;
