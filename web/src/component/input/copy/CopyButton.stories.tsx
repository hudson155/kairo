import { ComponentMeta, Story } from '@storybook/react';
import { ComponentProps } from 'react';
import CopyButton from './CopyButton';

interface Args {
  copy: 'Success' | 'NothingToCopy' | 'Failure';
}

export default {
  argTypes: {
    copy: {
      options: ['Success', 'NothingToCopy', 'Failure'],
      control: { type: 'radio' },
    },
  },
} as ComponentMeta<typeof CopyButton>;

const Template: Story<ComponentProps<typeof CopyButton> & Args> = ({ copy }) => {
  const handleCopy = (): string => {
    if (copy === 'Success') {
      return 'Copied content.';
    }
    if (copy === 'NothingToCopy') {
      return '';
    }
    throw new Error('Failed to copy.');
  };

  return <CopyButton onCopy={handleCopy} />;
};

export const Default = Template.bind({});
Default.args = {
  copy: 'Success',
};
