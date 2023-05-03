import { Meta, Story } from '@storybook/react';
import TextInput from 'component/input/TextInput';
import { ComponentProps, useState } from 'react';

interface Args {
  copyButton: boolean;
  hasError: boolean;
}

type StoryProps = ComponentProps<typeof TextInput> & Args;

const story: Meta<StoryProps> = {};

export default story;

const Template: Story<StoryProps> = ({ copyButton, hasError }) => {
  const [value, setValue] = useState('Limber');

  return (
    <TextInput
      copyButton={copyButton}
      errorMessage={hasError ? 'Error message.' : undefined}
      label="Name"
      placeholder="Acme Co."
      value={value}
      onChange={setValue}
    />
  );
};

export const Default = Template.bind({});
Default.args = {
  copyButton: true,
  hasError: false,
};
