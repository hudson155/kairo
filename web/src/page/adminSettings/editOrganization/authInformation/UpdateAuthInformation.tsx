import useUpdateOrganizationAuth from 'action/updateOrganizationAuth';
import Container from 'component/container/Container';
import Form from 'component/form/Form';
import { useFormField } from 'component/form/FormField';
import FormFields from 'component/form/FormFields';
import FormSubmitButton from 'component/form/submitButton/SubmitButton';
import Auth0OrganizationNameInput from 'page/adminSettings/editOrganization/authInformation/Auth0OrganizationNameInput';
import React from 'react';
import OrganizationAuthRep from 'rep/OrganizationAuthRep';

interface Props {
  auth: OrganizationAuthRep;
}

const UpdateAuthInformation: React.FC<Props> = ({ auth }) => {
  const updateAuth = useUpdateOrganizationAuth(auth.organizationGuid, auth.guid);

  const fields = new FormFields([
    ['body.auth0OrganizationName', useFormField(auth.auth0OrganizationName)],
  ]);

  const handleSubmit = async () => {
    await updateAuth({
      auth0OrganizationName: fields.getString('body.auth0OrganizationName').value,
    });
  };

  return (
    <Form fields={fields} onSubmit={handleSubmit}>
      <Container direction="vertical">
        <Auth0OrganizationNameInput />
        <FormSubmitButton>{'Update'}</FormSubmitButton>
      </Container>
    </Form>
  );
};

export default UpdateAuthInformation;
