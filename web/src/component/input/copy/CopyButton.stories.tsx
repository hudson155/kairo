import { ComponentMeta, Story } from '@storybook/react';
import { ComponentProps } from 'react';
import CopyButton from './CopyButton';

interface Args {
  copy: 'Success' | 'Failure';
}

export default {
  argTypes: {
    copy: {
      options: ['Success', 'Failure'],
      control: { type: 'radio' },
    },
  },
} as ComponentMeta<typeof CopyButton & Args>;

const Template: Story<ComponentProps<typeof CopyButton> & Args> = ({ copy }) => {
  const handleCopy = (): string => {
    if (copy === 'Success') {
      return 'Copied content.';
    }
    throw new Error('Failed to copy.');
  };

  return <CopyButton onCopy={handleCopy} />;
};

export const Default = Template.bind({});
Default.args = {
  copy: 'Success',
};
