import { ComponentMeta, Story } from '@storybook/react';
import Button from 'component/button/Button';
import { Variant } from 'component/button/ButtonDelegate';
import { ComponentProps } from 'react';

interface Args {
  variant: Variant;
}

export default {
  argTypes: {
    variant: {
      options: ['primary', 'unstyled'],
      control: { type: 'select' },
    },
  },
} as ComponentMeta<typeof Button>;

const Template: Story<ComponentProps<typeof Button> & Args> = ({ variant }) => {
  return (
    <Button variant={variant}>
      Button text
    </Button>
  );
};

export const Default = Template.bind({});
Default.args = {
  variant: 'primary',
};
