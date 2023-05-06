import { useForm } from 'component/form/Form';
import FormTextInput from 'component/form/input/FormTextInput';
import React from 'react';

const OrganizationNameInput: React.FC = () => {
  const { fields } = useForm();

  const field = fields.getString('body.name');

  return (
    <FormTextInput
      copyButton={true}
      field={field}
      label="Name"
      placeholder="Acme Co."
    />
  );
};

export default OrganizationNameInput;
