import { ComponentMeta, Story } from '@storybook/react';
import { ComponentProps } from 'react';
import LoadingPage from './LoadingPage';

export default {} as ComponentMeta<typeof LoadingPage>;

const Template: Story<ComponentProps<typeof LoadingPage>> = () => {
  return <LoadingPage />;
};

export const Default = Template.bind({});
