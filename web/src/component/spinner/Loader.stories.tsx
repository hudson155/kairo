import { ComponentMeta, Story } from '@storybook/react';
import { ComponentProps } from 'react';
import Loader from './Loader';

export default {} as ComponentMeta<typeof Loader>;

const Template: Story<ComponentProps<typeof Loader>> = () => {
  return <Loader />;
};

export const Default = Template.bind({});
