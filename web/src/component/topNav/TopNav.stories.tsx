import { ComponentMeta, ComponentStory } from '@storybook/react';
import TopNav from './TopNav';

export default {
  title: `TopNav`,
} as ComponentMeta<typeof TopNav>;

const Template: ComponentStory<typeof TopNav> = () => {
  return <TopNav left="Left" right="Right" />;
};

export const Default = Template.bind({});
