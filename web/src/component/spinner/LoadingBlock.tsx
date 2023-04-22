import Loader from 'component/spinner/Loader';
import styles from 'component/spinner/LoadingBlock.module.scss';
import React from 'react';

/**
 * This loading block is used while main components are loading.
 */
const LoadingBlock: React.FC = () => {
  return (
    <div className={styles.container}>
      <Loader />
    </div>
  );
};

export default LoadingBlock;
