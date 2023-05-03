import { Meta, Story } from '@storybook/react';
import CopyButton from 'component/input/copy/CopyButton';
import { ComponentProps } from 'react';

type State = 'Success' | 'NothingToCopy' | 'Failure';

interface Args {
  state: State;
}

type StoryProps = ComponentProps<typeof CopyButton> & Args;

const story: Meta<StoryProps> = {
  argTypes: {
    state: {
      options: ['Success', 'NothingToCopy', 'Failure'],
      control: { type: 'radio' },
    },
  },
};

export default story;

const Template: Story<StoryProps> = ({ state }) => {
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
