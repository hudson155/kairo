import Footer from 'component/footer/Footer';
import SideNavImpl from 'component/sideNav/SideNavImpl';
import TopNavImpl from 'component/topNav/TopNavImpl';
import ErrorBoundary from 'page/errorPage/ErrorBoundary';
import ErrorMain from 'page/errorPage/ErrorMain';
import React, { PropsWithChildren, ReactNode } from 'react';
import styles from './BaseLayout.module.scss';

interface Props extends PropsWithChildren {
  sideNav?: ReactNode;
  topNav?: ReactNode;
  children: ReactNode;
}

const BaseLayout: React.FC<Props> = ({ sideNav = <SideNavImpl />, topNav = <TopNavImpl />, children }) => {
  return (
    <div className={styles.outer}>
      {topNav}
      <div className={styles.middle}>
        {sideNav}
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
