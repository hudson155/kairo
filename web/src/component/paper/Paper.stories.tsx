import { Meta, Story } from '@storybook/react';
import Container from 'component/container/Container';
import Paper from 'component/paper/Paper';
import Heading2 from 'component/text/Heading2';
import Paragraph from 'component/text/Paragraph';
import { ComponentProps } from 'react';

type StoryProps = ComponentProps<typeof Paper>;

const story: Meta<StoryProps> = {
  argTypes: {
    variant: {
      options: ['default', 'danger'],
      control: { type: 'select' },
    },
  },
};

export default story;

const Template: Story<StoryProps> = ({ variant }) => {
  return (
    <Paper variant={variant === 'default' ? undefined : variant}>
      <Container direction="vertical">
        <Heading2>Some heading</Heading2>
        <Paragraph>Some content</Paragraph>
      </Container>
    </Paper>
  );
};

export const Default = Template.bind({});
Default.args = {
  variant: 'default',
};
