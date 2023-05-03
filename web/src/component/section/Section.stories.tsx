import { Meta, Story } from '@storybook/react';
import Container from 'component/container/Container';
import Page from 'component/page/Page';
import HeaderSection from 'component/section/HeaderSection';
import Section from 'component/section/Section';
import Paragraph from 'component/text/Paragraph';
import { ComponentProps } from 'react';
import * as Decorator from 'story/Decorator';

type StoryProps = ComponentProps<typeof Section>;

const story: Meta<StoryProps> = {
  decorators: [Decorator.helmetProvider()],
};

export default story;

const Template: Story<StoryProps> = () => {
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
