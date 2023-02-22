import { ComponentMeta, Story } from '@storybook/react';
import Container from 'component/container/Container';
import Page from 'component/page/Page';
import Section from 'component/section/Section';
import Heading1 from 'component/text/Heading1';
import Paragraph from 'component/text/Paragraph';
import { ComponentProps } from 'react';

export default {} as ComponentMeta<typeof Page>;

const Template: Story<ComponentProps<typeof Page>> = () => {
  return (
    <Page>
      <Section>
        <Container direction="vertical">
          <Heading1>First section</Heading1>
          <Paragraph>Some text in the first section.</Paragraph>
        </Container>
      </Section>
      <Section>
        <Container direction="vertical">
          <Heading1>Second section</Heading1>
          <Paragraph>Some text in the second section.</Paragraph>
        </Container>
      </Section>
    </Page>
  );
};

export const Default = Template.bind({});
