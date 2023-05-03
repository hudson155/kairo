import { Meta, Story } from '@storybook/react';
import Code from 'component/code/Code';
import Paragraph from 'component/text/Paragraph';
import { ComponentProps } from 'react';

interface Args {
  selectAll: boolean;
}

type StoryProps = ComponentProps<typeof Code> & Args;

const story: Meta<StoryProps> = {};

export default story;

const Template: Story<StoryProps> = ({ selectAll }) => {
  return (
    <Paragraph>
      Use <Code selectAll={selectAll ? true : undefined}>yarn start</Code> to start the app.
    </Paragraph>
  );
};

export const Default = Template.bind({});
Default.args = {
  selectAll: false,
};
