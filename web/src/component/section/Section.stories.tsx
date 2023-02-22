import { ComponentMeta, Story } from '@storybook/react';
import Container from 'component/container/Container';
import Page from 'component/page/Page';
import Paragraph from 'component/text/Paragraph';
import { ComponentProps } from 'react';
import * as Decorator from 'story/Decorator';
import HeaderSection from './HeaderSection';
import Section from './Section';

export default {
  decorators: [Decorator.helmetProvider()],
} as ComponentMeta<typeof Section>;

const Template: Story<ComponentProps<typeof Section>> = () => {
  return (
    <Page>
      <HeaderSection title="Section title">
        <Paragraph>The content of the header section.</Paragraph>
      </HeaderSection>
      <Section>
        <Container direction="vertical">
          <Paragraph>The content of a subsequent section.</Paragraph>
          <Paragraph>Further content of the subsequent section.</Paragraph>
        </Container>
      </Section>
    </Page>
  );
};

export const Default = Template.bind({});
