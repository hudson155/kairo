import Container from 'component/container/Container';
import Section from 'component/section/Section';
import Heading1 from 'component/text/Heading1';
import React, { ReactNode } from 'react';
import { Helmet } from 'react-helmet-async';

interface Props {
  title: string;
  children?: ReactNode;
}

/**
 * A special type of {@link Section} that appears first within the main area of a page.
 * Has a built-in heading, and sets the page title to match.
 */
const HeaderSection: React.FC<Props> = ({ title, children = undefined }) => {
  return (
    <Section>
      <Helmet>
        <title>{pageTitle(title)}</title>
      </Helmet>
      <Container direction="vertical">
        <Heading1>{title}</Heading1>
        {children ?? null}
      </Container>
    </Section>
  );
};

export default HeaderSection;

const pageTitle = (title: string): string => `${title} | Limber`;
