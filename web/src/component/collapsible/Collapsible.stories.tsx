import { Meta, Story } from '@storybook/react';
import Collapsible from 'component/collapsible/Collapsible';
import styles from 'component/collapsible/Collapsible.stories.module.scss';
import Paragraph from 'component/text/Paragraph';
import { ComponentProps } from 'react';

interface Args {
  isOpen: boolean;
}

type StoryProps = ComponentProps<typeof Collapsible> & Args;

const story: Meta<StoryProps> = {};

export default story;

const Template: Story<StoryProps> = ({ isOpen }) => {
  return (
    <Collapsible isOpen={isOpen}>
      <Paragraph className={styles.paragraph}>
        Collapsible content, with background color to best illustrate the transition.
      </Paragraph>
    </Collapsible>
  );
};

export const Default = Template.bind({});
Default.args = {
  isOpen: false,
};
