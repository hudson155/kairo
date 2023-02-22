import { ComponentMeta, Story } from '@storybook/react';
import Code from 'component/code/Code';
import Paragraph from 'component/text/Paragraph';
import { ComponentProps } from 'react';

interface Args {
  selectAll: boolean;
}

export default {} as ComponentMeta<typeof Code>;

const Template: Story<ComponentProps<typeof Code> & Args> = ({ selectAll }) => {
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
