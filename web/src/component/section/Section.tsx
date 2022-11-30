import React, { ReactNode } from 'react';
import styles from './Section.module.scss';

interface Props {
  children: ReactNode;
}

/**
 * A top-level section within the main area of a page.
 */
const Section: React.FC<Props> = ({ children }) => {
  return (
    <section className={styles.section}>
      {children}
    </section>
  );
};

export default Section;
