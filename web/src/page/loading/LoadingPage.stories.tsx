import { Meta, Story } from '@storybook/react';
import LoadingPage from 'page/loading/LoadingPage';
import { ComponentProps } from 'react';

type StoryProps = ComponentProps<typeof LoadingPage>;

const story: Meta<StoryProps> = {};

export default story;

const Template: Story<StoryProps> = () => {
  return <LoadingPage />;
};

export const Default = Template.bind({});
