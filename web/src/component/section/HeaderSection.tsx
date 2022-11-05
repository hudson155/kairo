import Section from 'component/section/Section';
import Heading1 from 'component/text/Heading1';
import React, { PropsWithChildren, ReactNode } from 'react';
import { Helmet } from 'react-helmet-async';

interface Props extends PropsWithChildren {
  title: string;
  children: ReactNode;
}

/**
 * A special type of [Section] that appears first within the main area of a page.
 * Has a built-in heading, and sets the page title to match.
 */
const HeaderSection: React.FC<Props> = ({ title, children }) => {
  return (
    <Section>
      <Helmet>
        <title>{title} | Limber</title>
      </Helmet>
      <Heading1>{title}</Heading1>
      {children}
    </Section>
  );
};

export default HeaderSection;
