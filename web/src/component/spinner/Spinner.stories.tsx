import { ComponentMeta, Story } from '@storybook/react';
import { ComponentProps } from 'react';
import Spinner from './Spinner';

export default {} as ComponentMeta<typeof Spinner>;

const Template: Story<ComponentProps<typeof Spinner>> = () => {
  return <Spinner />;
};

export const Default = Template.bind({});
