import { Meta, Story } from '@storybook/react';
import SubmitButton from 'component/form/submitButton/SubmitButtonDelegate';
import { ComponentProps } from 'react';

interface Args {
  isSubmitting: boolean;
}

type StoryProps = ComponentProps<typeof SubmitButton> & Args;

const story: Meta<StoryProps> = {
  argTypes: {
    variant: {
      options: ['primary', 'danger'],
      control: { type: 'select' },
    },
  },
};

export default story;

const Template: Story<StoryProps> = ({ isSubmitting, variant }) => {
  return (
    <SubmitButton isSubmitting={isSubmitting} variant={variant === 'primary' ? undefined : variant}>
      Button text
    </SubmitButton>
  );
};

export const Default = Template.bind({});
Default.args = {
  isSubmitting: false,
  variant: 'primary',
};
