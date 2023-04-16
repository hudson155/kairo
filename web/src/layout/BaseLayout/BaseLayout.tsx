import Footer from 'component/footer/Footer';
import SideNavImpl from 'component/sideNav/SideNavImpl';
import TopNavImpl from 'component/topNav/TopNavImpl';
import styles from 'layout/BaseLayout/BaseLayout.module.scss';
import ErrorBoundary from 'page/error/ErrorBoundary';
import ErrorMain from 'page/error/ErrorMain';
import React, { ReactNode } from 'react';

interface Props {
  sideNav?: ReactNode;
  topNav?: ReactNode;
  children: ReactNode;
}

const BaseLayout: React.FC<Props> = ({ sideNav = <SideNavImpl />, topNav = <TopNavImpl />, children }) => {
  return (
    <div className={styles.outer}>
      <div className={styles.topNav}>{topNav}</div>
      <div className={styles.middle}>
        <div className={styles.sideNav}>{sideNav}</div>
        <div className={styles.inner}>
          <main className={styles.main}>
            <ErrorBoundary fallback={ErrorMain.fallback}>
              {children}
            </ErrorBoundary>
          </main>
          <Footer />
        </div>
      </div>
    </div>
  );
};

export default BaseLayout;
