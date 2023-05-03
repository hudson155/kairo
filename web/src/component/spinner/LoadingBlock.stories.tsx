import { Meta, Story } from '@storybook/react';
import LoadingBlock from 'component/spinner/LoadingBlock';
import { ComponentProps } from 'react';

type StoryProps = ComponentProps<typeof LoadingBlock>;

const story: Meta<StoryProps> = {};

export default story;

const Template: Story<StoryProps> = () => {
  return <LoadingBlock />;
};

export const Default = Template.bind({});
