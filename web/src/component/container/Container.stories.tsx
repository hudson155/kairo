import { ComponentMeta, Story } from '@storybook/react';
import Paragraph from 'component/text/Paragraph';
import { ComponentProps } from 'react';
import Container from './Container';

export default {
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
} as ComponentMeta<typeof Container>;

const Template: Story<ComponentProps<typeof Container>> = ({ density, direction }) => {
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
