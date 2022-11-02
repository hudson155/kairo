import { ComponentMeta, ComponentStory } from '@storybook/react';
import LoadingPage from './LoadingPage';

export default {
  title: 'LoadingPage',
  component: LoadingPage,
} as ComponentMeta<typeof LoadingPage>;

const Template: ComponentStory<typeof LoadingPage> = (args) => <LoadingPage {...args} />;

export const Default = Template.bind({});
