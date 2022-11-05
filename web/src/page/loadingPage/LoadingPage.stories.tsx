import { ComponentMeta, ComponentStory } from '@storybook/react';
import LoadingPage from './LoadingPage';

export default {
  title: 'LoadingPage',
  component: LoadingPage,
} as ComponentMeta<typeof LoadingPage>;

const Template: ComponentStory<typeof LoadingPage> = () => {
  return <LoadingPage />;
};

export const Default = Template.bind({});