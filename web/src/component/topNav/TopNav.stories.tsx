import { Meta, Story } from '@storybook/react';
import TopNav from 'component/topNav/TopNav';
import { ComponentProps } from 'react';

type StoryProps = ComponentProps<typeof TopNav>;

const story: Meta<StoryProps> = {};

export default story;

const Template: Story<StoryProps> = () => {
  return <TopNav left="Left" right="Right" />;
};

export const Default = Template.bind({});
