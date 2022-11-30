import { ComponentMeta, Story } from '@storybook/react';
import doNothing from 'helper/doNothing';
import { ComponentProps } from 'react';
import Button from './Button';

export default {} as ComponentMeta<typeof Button>;

const Template: Story<ComponentProps<typeof Button>> = () => {
  return <Button variant="unstyled" onClick={doNothing}>Button text</Button>;
};

export const Default = Template.bind({});
