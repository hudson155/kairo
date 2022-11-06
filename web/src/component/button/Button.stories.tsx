import { ComponentMeta, ComponentStory } from '@storybook/react';
import Text from 'component/text/Text';
import Button from './Button';

export default {
  title: `Button`,
} as ComponentMeta<typeof Button>;

const Template: ComponentStory<typeof Button> = () => {
  return <Button variant="unstyled"><Text>Button text</Text></Button>;
};

export const Default = Template.bind({});
