import { Meta, Story } from '@storybook/react';
import Loader from 'component/spinner/Loader';
import { ComponentProps } from 'react';

type StoryProps = ComponentProps<typeof Loader>;

const story: Meta<StoryProps> = {};

export default story;

const Template: Story<StoryProps> = () => {
  return <Loader />;
};

export const Default = Template.bind({});
