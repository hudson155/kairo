import { Meta, Story } from '@storybook/react';
import Button from 'component/button/Button';
import Container from 'component/container/Container';
import { ComponentProps } from 'react';

type StoryProps = ComponentProps<typeof Button>;

const story: Meta<StoryProps> = {};

export default story;

const Template: Story<StoryProps> = ({ disabled }) => {
  return (
    <Container direction="vertical">
      <Button disabled={disabled} variant="primary">Primary button</Button>
      <Button disabled={disabled} variant="secondary">Secondary button</Button>
      <Button disabled={disabled} variant="danger">Danger button</Button>
      <Button disabled={disabled} variant="unstyled">Unstyled button</Button>
    </Container>
  );
};

export const Default = Template.bind({});
Default.args = {
  disabled: false,
};
