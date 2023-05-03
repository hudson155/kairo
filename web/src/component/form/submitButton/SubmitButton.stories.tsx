import { Meta, Story } from '@storybook/react';
import SubmitButton from 'component/form/submitButton/SubmitButtonDelegate';
import { ComponentProps } from 'react';

interface Args {
  isSubmitting: boolean;
}

type StoryProps = ComponentProps<typeof SubmitButton> & Args;

const story: Meta<StoryProps> = {};

export default story;

const Template: Story<StoryProps> = ({ isSubmitting }) => {
  return (
    <SubmitButton isSubmitting={isSubmitting}>Button text</SubmitButton>
  );
};

export const Default = Template.bind({});
Default.args = {
  isSubmitting: false,
};
