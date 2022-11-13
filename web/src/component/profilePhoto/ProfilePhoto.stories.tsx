import { ComponentMeta, ComponentStory } from '@storybook/react';
import { Delegate as ProfilePhoto } from './ProfilePhoto';

export default {
  title: `component/ProfilePhoto`,
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
