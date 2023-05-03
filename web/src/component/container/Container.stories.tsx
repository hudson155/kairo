import { Meta, Story } from '@storybook/react';
import Container from 'component/container/Container';
import Paragraph from 'component/text/Paragraph';
import { ComponentProps } from 'react';

type StoryProps = ComponentProps<typeof Container>;

const story: Meta<StoryProps> = {
  argTypes: {
    density: {
      options: ['very-compact', 'compact', 'normal', 'relaxed'],
      control: { type: 'select' },
    },
    direction: {
      options: ['horizontal', 'vertical'],
      control: { type: 'select' },
    },
  },
};

export default story;

const Template: Story<StoryProps> = ({ density, direction }) => {
  return (
    <Container density={density === 'normal' ? undefined : density} direction={direction}>
      <Paragraph>First paragraph.</Paragraph>
      <Paragraph>Second paragraph.</Paragraph>
    </Container>
  );
};

export const Default = Template.bind({});
Default.args = {
  density: 'normal',
  direction: 'vertical',
};
