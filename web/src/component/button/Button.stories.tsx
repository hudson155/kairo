import { ComponentMeta, Story } from '@storybook/react';
import { ComponentProps } from 'react';
import Button from './Button';

export default {} as ComponentMeta<typeof Button>;

const Template: Story<ComponentProps<typeof Button>> = () => {
  return <Button variant="unstyled">Button text</Button>;
};

export const Default = Template.bind({});
