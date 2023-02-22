import { ComponentMeta, Story } from '@storybook/react';
import TopNav from 'component/topNav/TopNav';
import { ComponentProps } from 'react';

export default {} as ComponentMeta<typeof TopNav>;

const Template: Story<ComponentProps<typeof TopNav>> = () => {
  return <TopNav left="Left" right="Right" />;
};

export const Default = Template.bind({});
