import Spinner from 'component/spinner/Spinner';
import React from 'react';
import styles from 'component/button/ButtonOverlay.module.scss';

interface Props {
  isSubmitting: boolean;
}

const ButtonOverlay: React.FC<Props> = ({ isSubmitting }) => {
  return (
    <div className={styles.overlay}>
      {isSubmitting ? <Spinner /> : null}
    </div>
  );
};

export default ButtonOverlay;
