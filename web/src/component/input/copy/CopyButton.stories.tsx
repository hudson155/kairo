import { ComponentMeta, Story } from '@storybook/react';
import { ComponentProps } from 'react';
import CopyButton from './CopyButton';
import styles from './CopyButton.stories.module.scss';

interface Args {
  copy: 'Success' | 'Failure';
}

export default {
  argTypes: {
    copy: {
      options: [`Success`, `Failure`],
      control: { type: `radio` },
    },
  },
} as ComponentMeta<typeof CopyButton & Args>;

const Template: Story<ComponentProps<typeof CopyButton> & Args> = ({ copy }) => {
  const handleCopy = (): string => {
    if (copy === `Success`) {
      return `Copied content.`;
    }
    throw new Error(`Failed to copy.`);
  };

  return (
    <div className={styles.container}>
      <CopyButton onCopy={handleCopy} />
    </div>
  );
};

export const Default = Template.bind({});
Default.args = {
  copy: `Success`,
};
