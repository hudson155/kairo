import { Meta, Story } from '@storybook/react';
import Banner from 'component/banner/Banner';
import NotFoundBanner from 'component/banner/NotFoundBanner';
import { ComponentProps } from 'react';

type StoryProps = ComponentProps<typeof Banner>;

const story: Meta<StoryProps> = {};

export default story;

const Template: Story<StoryProps> = () => {
  return <NotFoundBanner entity="user" />;
};

export const Default = Template.bind({});
