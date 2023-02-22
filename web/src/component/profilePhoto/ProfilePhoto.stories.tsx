import { ComponentMeta, Story } from '@storybook/react';
import Container from 'component/container/Container';
import { ComponentProps } from 'react';
import ProfilePhoto from './ProfilePhotoDelegate';

export default {} as ComponentMeta<typeof ProfilePhoto>;

const Template: Story<ComponentProps<typeof ProfilePhoto>> = () => {
  return (
    <Container direction="horizontal">
      <ProfilePhoto url={undefined} />
      <ProfilePhoto url="https://avatars.githubusercontent.com/u/1360420" />
    </Container>
  );
};

export const Default = Template.bind({});
