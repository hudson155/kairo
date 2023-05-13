import { useForm } from 'component/form/Form';
import FormTextInput from 'component/form/input/FormTextInput';
import React from 'react';

const Auth0OrganizationNameInput: React.FC = () => {
  const { fields } = useForm();

  const field = fields.getString('body.auth0OrganizationName');

  return (
    <FormTextInput
      copyButton={true}
      field={field}
      label="Auth0 organization name"
      placeholder="acme-co"
    />
  );
};

export default Auth0OrganizationNameInput;
