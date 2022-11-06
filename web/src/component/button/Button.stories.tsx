import { ComponentMeta, ComponentStory } from '@storybook/react';
import Button from './Button';

export default {
  title: `Button`,
} as ComponentMeta<typeof Button>;

const Template: ComponentStory<typeof Button> = () => {
  return <Button variant="unstyled">Button text</Button>;
};

export const Default = Template.bind({});
