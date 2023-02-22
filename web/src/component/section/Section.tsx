import React, { ReactNode } from 'react';

interface Props {
  children: ReactNode;
}

/**
 * A top-level section within the main area of a page.
 */
const Section: React.FC<Props> = ({ children }) => {
  return (
    <section>
      {children}
    </section>
  );
};

export default Section;
