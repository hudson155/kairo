import Loader from 'component/spinner/Loader';
import React from 'react';
import styles from './LoadingPage.module.scss';

/**
 * This loading page is used while the root app is loading.
 * NOTE: It should be kept as lightweight as possible by intentionally avoiding the inclusion of other components.
 * This will help with first paint performance by keeping the initial bundle small.
 */
const LoadingPage: React.FC = () => {
  return (
    <div className={styles.container}>
      <Loader />
    </div>
  );
};

export default LoadingPage;
