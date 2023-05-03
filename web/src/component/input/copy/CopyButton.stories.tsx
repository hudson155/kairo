import { ComponentMeta, Story } from '@storybook/react';
import CopyButton from 'component/input/copy/CopyButton';
import { ComponentProps } from 'react';

type State = 'Success' | 'NothingToCopy' | 'Failure';

interface Args {
  state: State;
}

export default {
  argTypes: {
    state: {
      options: ['Success', 'NothingToCopy', 'Failure'],
      control: { type: 'radio' },
    },
  },
} as ComponentMeta<typeof CopyButton>;

const Template: Story<ComponentProps<typeof CopyButton> & Args> = ({ state }) => {
  const handleCopy = (): string => {
    switch (state) {
    case 'Success':
      return 'Copied content.';
    case 'NothingToCopy':
      return '';
    case 'Failure':
      throw new Error('Failed to copy.');
    }
  };

  return <CopyButton onCopy={handleCopy} />;
};

export const Default = Template.bind({});
Default.args = {
  state: 'Success',
};
