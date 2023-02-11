import { ComponentMeta, Story } from '@storybook/react';
import Button from 'component/button/Button';
import InputGroup from 'component/input/group/InputGroup';
import { ComponentProps, MouseEventHandler } from 'react';
import * as Decorator from 'story/Decorator';
import ToastContainer, { useToast } from './ToastContainer';

export default {
  decorators: [Decorator.toastProvider()],
} as ComponentMeta<typeof ToastContainer>;

const Template: Story<ComponentProps<typeof ToastContainer>> = () => {
  const toast = useToast();

  const handleSuccessClick: MouseEventHandler<HTMLButtonElement> = () => {
    toast.success('Success!');
  };

  const handleFailureClick: MouseEventHandler<HTMLButtonElement> = () => {
    toast.failure('Failure!');
  };

  return (
    <InputGroup>
      <Button variant="primary" onClick={handleSuccessClick}>Show success toast</Button>
      <Button variant="primary" onClick={handleFailureClick}>Show failure toast</Button>
    </InputGroup>
  );
};

export const Default = Template.bind({});
