import { ComponentMeta, Story } from '@storybook/react';
import LoadingPage from 'page/loading/LoadingPage';
import { ComponentProps } from 'react';

export default {} as ComponentMeta<typeof LoadingPage>;

const Template: Story<ComponentProps<typeof LoadingPage>> = () => {
  return <LoadingPage />;
};

export const Default = Template.bind({});
