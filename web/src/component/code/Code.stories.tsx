import { ComponentMeta, ComponentStory } from '@storybook/react';
import Paragraph from 'component/text/Paragraph';
import Code from './Code';

export default {
  title: `Code`,
} as ComponentMeta<typeof Code>;

const Template: ComponentStory<typeof Code> = () => {
  return (
    <Paragraph>
      Use <Code>yarn start</Code> to start the app.
    </Paragraph>
  );
};

export const Default = Template.bind({});
