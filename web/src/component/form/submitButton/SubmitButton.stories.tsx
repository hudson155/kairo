import { ComponentMeta, Story } from '@storybook/react';
import SubmitButton from 'component/form/submitButton/SubmitButtonDelegate';
import { ComponentProps } from 'react';

interface Args {
  isSubmitting: boolean;
}

export default {} as ComponentMeta<typeof SubmitButton>;

const Template: Story<ComponentProps<typeof SubmitButton> & Args> = ({ isSubmitting }) => {
  return (
    <SubmitButton isSubmitting={isSubmitting}>Button text</SubmitButton>
  );
};

export const Default = Template.bind({});
Default.args = {
  isSubmitting: false,
};
