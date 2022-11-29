import { ComponentMeta, Story } from '@storybook/react';
import { ComponentProps } from 'react';
import Paragraph from 'component/text/Paragraph';
import Code from './Code';

export default {} as ComponentMeta<typeof Code>;

const Template: Story<ComponentProps<typeof Code>> = () => {
  return (
    <Paragraph>
      Use <Code>yarn start</Code> to start the app.
    </Paragraph>
  );
};

export const Default = Template.bind({});
