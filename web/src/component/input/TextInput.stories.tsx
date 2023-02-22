import { ComponentMeta, Story } from '@storybook/react';
import TextInput from 'component/input/TextInput';
import { ComponentProps, useState } from 'react';

interface Args {
  copyButton: boolean;
  hasError: boolean;
}

export default {} as ComponentMeta<typeof TextInput>;

const Template: Story<ComponentProps<typeof TextInput> & Args> = ({ copyButton, hasError }) => {
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
