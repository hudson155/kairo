import { ComponentMeta, Story } from '@storybook/react';
import Collapsible from 'component/collapsible/Collapsible';
import styles from 'component/collapsible/Collapsible.stories.module.scss';
import Paragraph from 'component/text/Paragraph';
import React, { ComponentProps } from 'react';

interface Args {
  isOpen: boolean;
}

export default {} as ComponentMeta<typeof Impl>;

const Template: Story<ComponentProps<typeof Impl> & Args> = ({ isOpen }) => {
  return <Impl isOpen={isOpen} />;
};

export const Default = Template.bind({});
Default.args = {
  isOpen: false,
};

const Impl: React.FC<Args> = ({ isOpen }) => {
  return (
    <Collapsible isOpen={isOpen}>
      <Paragraph className={styles.paragraph}>
        Collapsible content, with background color to best illustrate the transition.
      </Paragraph>
    </Collapsible>
  );
};
