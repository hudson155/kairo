import { ComponentMeta, Story } from '@storybook/react';
import { ComponentProps } from 'react';
import ProfilePhoto from './ProfilePhotoDelegate';

export default {} as ComponentMeta<typeof ProfilePhoto>;

const Template: Story<ComponentProps<typeof ProfilePhoto>> = () => {
  return (
    <>
      <ProfilePhoto url={undefined} />
      <ProfilePhoto url="https://avatars.githubusercontent.com/u/1360420" />
    </>
  );
};

export const Default = Template.bind({});
