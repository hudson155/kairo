import useCreateOrganizationAuth from 'action/createOrganizationAuth';
import Banner from 'component/banner/Banner';
import Button from 'component/button/Button';
import Container from 'component/container/Container';
import Form from 'component/form/Form';
import { useFormField } from 'component/form/FormField';
import FormFields from 'component/form/FormFields';
import FormSubmitButton from 'component/form/submitButton/SubmitButton';
import Paragraph from 'component/text/Paragraph';
import Auth0OrganizationNameInput from 'page/adminSettings/editOrganization/authInformation/Auth0OrganizationNameInput';
import React, { useState } from 'react';
import OrganizationRep from 'rep/OrganizationRep';
import slugify from 'slugify';

interface Props {
  organization: OrganizationRep;
}

const CreateAuthInformation: React.FC<Props> = ({ organization }) => {
  const createAuth = useCreateOrganizationAuth(organization.guid);

  const [showForm, setShowForm] = useState(false);

  const fields = new FormFields([
    ['body.auth0OrganizationName', useFormField(defaultAuth0OrganizationName(organization.name))],
  ]);

  const handleSubmit = async () => {
    await createAuth({
      auth0OrganizationName: fields.getString('body.auth0OrganizationName').value,
    });
  };

  if (!showForm) {
    return (
      <>
        <Banner variant="warning">
          <Paragraph>{'This organization doesn\'t have auth configured.'}</Paragraph>
        </Banner>
        <Button variant="primary" onClick={() => setShowForm(true)}>{'Configure auth'}</Button>
      </>
    );
  }

  return (
    <Form fields={fields} onSubmit={handleSubmit}>
      <Container direction="vertical">
        <Auth0OrganizationNameInput />
        <Container direction="horizontal">
          <FormSubmitButton>{'Create'}</FormSubmitButton>
          <Button variant="secondary" onClick={() => setShowForm(false)}>{'Cancel'}</Button>
        </Container>
      </Container>
    </Form>
  );
};

export default CreateAuthInformation;

export const defaultAuth0OrganizationName = (organizationName: string): string => {
  return slugify(organizationName, { lower: true });
};
