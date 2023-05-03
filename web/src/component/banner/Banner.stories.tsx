import { Meta, Story } from '@storybook/react';
import Banner from 'component/banner/Banner';
import Container from 'component/container/Container';
import { ComponentProps } from 'react';

type StoryProps = ComponentProps<typeof Banner>;

const story: Meta<StoryProps> = {};

export default story;

const Template: Story<StoryProps> = () => {
  return (
    <Container direction="vertical">
      <Banner variant="success">Success banner.</Banner>
      <Banner variant="warning">Warning banner.</Banner>
      <Banner variant="danger">Danger banner.</Banner>
    </Container>
  );
};

export const Default = Template.bind({});
