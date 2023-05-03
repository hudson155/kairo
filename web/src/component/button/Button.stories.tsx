import { Meta, Story } from '@storybook/react';
import Button from 'component/button/Button';
import Container from 'component/container/Container';
import { ComponentProps } from 'react';

type StoryProps = ComponentProps<typeof Button>;

const story: Meta<StoryProps> = {};

export default story;

const Template: Story<StoryProps> = () => {
  return (
    <Container direction="vertical">
      <Button variant="unstyled">Button text</Button>
      <Button variant="primary">Button text</Button>
      <Button variant="secondary">Button text</Button>
    </Container>
  );
};

export const Default = Template.bind({});
