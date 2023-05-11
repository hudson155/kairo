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
      <Button variant="primary">Primary button</Button>
      <Button variant="secondary">Secondary button</Button>
      <Button variant="danger">Danger button</Button>
      <Button variant="unstyled">Unstyled button</Button>
    </Container>
  );
};

export const Default = Template.bind({});
