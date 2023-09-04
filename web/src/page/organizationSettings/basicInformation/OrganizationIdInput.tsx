import TextInput from 'component/input/TextInput';
import { doNothing } from 'helper/doNothing';
import React from 'react';

interface Props {
  value: string;
}

const OrganizationIdInput: React.FC<Props> = ({ value }) => {
  return (
    <TextInput
      copyButton={true}
      disabled={true}
      label="ID"
      value={value}
      onChange={doNothing}
    />
  );
};

export default OrganizationIdInput;
