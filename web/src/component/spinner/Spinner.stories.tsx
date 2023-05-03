import { Meta, Story } from '@storybook/react';
import Spinner from 'component/spinner/Spinner';
import { ComponentProps } from 'react';

type StoryProps = ComponentProps<typeof Spinner>;

const story: Meta<StoryProps> = {};

export default story;

const Template: Story<StoryProps> = () => {
  return <Spinner />;
};

export const Default = Template.bind({});
