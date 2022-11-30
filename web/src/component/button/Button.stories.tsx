import { ComponentMeta, Story } from '@storybook/react';
import doNothing from 'helper/doNothing';
import { ComponentProps } from 'react';
import Button, { Variant } from './ButtonDelegate';

interface Args {
  isSubmitting: boolean;
  variant: Variant;
}

export default {
  argTypes: {
    variant: {
      options: [`primary`, `unstyled`],
      control: { type: `select` },
    },
  },
} as ComponentMeta<typeof Button & Args>;

const Template: Story<ComponentProps<typeof Button & Args>> = ({ isSubmitting, variant }) => {
  return (
    <Button isSubmitting={isSubmitting} type="button" variant={variant} onClick={doNothing}>
      Button text
    </Button>
  );
};

export const Default = Template.bind({});
Default.args = {
  isSubmitting: false,
  variant: `primary`,
};
