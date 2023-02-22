import { ComponentMeta, Story } from '@storybook/react';
import Button from 'component/button/Button';
import Container from 'component/container/Container';
import ToastContainer, { useToast } from 'component/toast/ToastContainer';
import { ComponentProps } from 'react';
import * as Decorator from 'story/Decorator';

export default {
  decorators: [Decorator.toastProvider()],
} as ComponentMeta<typeof ToastContainer>;

const Template: Story<ComponentProps<typeof ToastContainer>> = () => {
  const toast = useToast();

  const handleSuccessClick = () => {
    toast.success('Success!');
  };

  const handleFailureClick = () => {
    toast.failure('Failure!');
  };

  return (
    <Container direction="horizontal">
      <Button variant="primary" onClick={handleSuccessClick}>Show success toast</Button>
      <Button variant="primary" onClick={handleFailureClick}>Show failure toast</Button>
    </Container>
  );
};

export const Default = Template.bind({});
