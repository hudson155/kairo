import { Meta, Story } from '@storybook/react';
import Paragraph from 'component/text/Paragraph';
import { useResizeObserver } from 'hook/useResizeObserver';

interface StoryProps {
}

const story: Meta<StoryProps> = {};

export default story;

const Template: Story<StoryProps> = () => {
  const isSmall = useResizeObserver((size) => size < 992);
  return <Paragraph>{isSmall ? 'Small' : 'Large'}</Paragraph>;
};

export const Default = Template.bind({});
