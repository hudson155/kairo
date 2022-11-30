import { ComponentMeta, Story } from '@storybook/react';
import { ComponentProps, useState } from 'react';
import TextInput from './TextInput';

interface Args {
  copyButton: boolean;
}

export default {} as ComponentMeta<typeof TextInput & Args>;

const Template: Story<ComponentProps<typeof TextInput & Args>> = ({ copyButton }) => {
  const [value, setValue] = useState(`Limber`);

  return <TextInput copyButton={copyButton} label="Name" value={value} onChange={setValue} />;
};

export const Default = Template.bind({});
Default.args = {
  copyButton: true,
};
