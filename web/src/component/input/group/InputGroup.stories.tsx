import { ComponentMeta, Story } from '@storybook/react';
import TextInput from 'component/input/TextInput';
import { ComponentProps, useState } from 'react';
import InputGroup from './InputGroup';

export default {} as ComponentMeta<typeof InputGroup>;

const Template: Story<ComponentProps<typeof InputGroup>> = () => {
  const [field1, setField1] = useState('');
  const [field2, setField2] = useState('');
  const [field3, setField3] = useState('');

  return (
    <InputGroup>
      <TextInput label="Regular field 1" value={field1} onChange={setField1} />
      <TextInput label="Regular field 2" value={field2} onChange={setField2} />
      <TextInput label="Wide field" value={field3} width={2} onChange={setField3} />
    </InputGroup>
  );
};

export const Default = Template.bind({});
