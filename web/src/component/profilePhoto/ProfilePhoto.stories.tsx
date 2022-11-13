import { ComponentMeta, ComponentStory } from '@storybook/react';
import * as Decorator from 'story/Decorator';
import { Delegate as ProfilePhoto } from './ProfilePhoto';

export default {
  title: `component/ProfilePhoto`,
  decorators: [Decorator.browserRouter()],
} as ComponentMeta<typeof ProfilePhoto>;

const Template: ComponentStory<typeof ProfilePhoto> = () => {
  return (
    <>
      <ProfilePhoto url={undefined} />
      <ProfilePhoto url="https://avatars.githubusercontent.com/u/1360420" />
    </>
  );
};

export const Default = Template.bind({});
