import { Meta, Story } from '@storybook/react';
import Container from 'component/container/Container';
import ProfilePhoto from 'component/profilePhoto/ProfilePhotoDelegate';
import { ComponentProps } from 'react';

type StoryProps = ComponentProps<typeof ProfilePhoto>;

const story: Meta<StoryProps> = {};

export default story;

const Template: Story<StoryProps> = () => {
  return (
    <Container direction="horizontal">
      <ProfilePhoto url={undefined} />
      <ProfilePhoto url="https://avatars.githubusercontent.com/u/1360420" />
    </Container>
  );
};

export const Default = Template.bind({});
