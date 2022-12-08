import { ComponentMeta, Story } from '@storybook/react';
import Heading2 from 'component/text/Heading2';
import Paragraph from 'component/text/Paragraph';
import { ComponentProps } from 'react';
import Paper from './Paper';

export default {} as ComponentMeta<typeof Paper>;

const Template: Story<ComponentProps<typeof Paper>> = () => {
  return (
    <Paper>
      <Heading2>Some heading</Heading2>
      <Paragraph>Some content</Paragraph>
    </Paper>
  );
};

export const Default = Template.bind({});
