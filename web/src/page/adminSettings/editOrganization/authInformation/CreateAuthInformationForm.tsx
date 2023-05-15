import useCreateOrganizationAuth from 'action/createOrganizationAuth';
import Button from 'component/button/Button';
import Container from 'component/container/Container';
import Form from 'component/form/Form';
import { useFormField } from 'component/form/FormField';
import FormFields from 'component/form/FormFields';
import FormSubmitButton from 'component/form/submitButton/SubmitButton';
import Auth0OrganizationNameInput from 'page/adminSettings/editOrganization/authInformation/Auth0OrganizationNameInput';
import React from 'react';
import OrganizationRep from 'rep/OrganizationRep';
import slugify from 'slugify';

interface Props {
  organization: OrganizationRep;
  onCancel: () => void;
}

const CreateAuthInformationForm: React.FC<Props> = ({ organization, onCancel }) => {
  const createAuth = useCreateOrganizationAuth(organization.guid);

  const fields = new FormFields([
    ['body.auth0OrganizationName', useFormField(defaultAuth0OrganizationName(organization.name))],
  ]);

  const handleSubmit = async () => {
    await createAuth({
      auth0OrganizationName: fields.getString('body.auth0OrganizationName').value,
    });
  };

  return (
    <Form fields={fields} onSubmit={handleSubmit}>
      <Container direction="vertical">
        <Auth0OrganizationNameInput />
        <Container direction="horizontal">
          <FormSubmitButton>{'Create'}</FormSubmitButton>
          <Button variant="secondary" onClick={onCancel}>{'Cancel'}</Button>
        </Container>
      </Container>
    </Form>
  );
};

export default CreateAuthInformationForm;

export const defaultAuth0OrganizationName = (organizationName: string): string => {
  return slugify(organizationName, { lower: true });
};
