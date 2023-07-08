import useUpdateOrganizationAuth from 'action/updateOrganizationAuth';
import useDeleteOrganizationAuth from 'action/useDeleteOrganizationAuth';
import Button from 'component/button/Button';
import Container from 'component/container/Container';
import Form from 'component/form/Form';
import { useFormField } from 'component/form/FormField';
import FormFields from 'component/form/FormFields';
import FormSubmitButton from 'component/form/submitButton/SubmitButton';
import Auth0OrganizationNameInput from 'page/adminSettings/editOrganization/authInformation/Auth0OrganizationNameInput';
import DeleteConfirmation from 'page/adminSettings/editOrganization/authInformation/DeleteConfirmation';
import styles from 'page/adminSettings/editOrganization/authInformation/UpdateAuthInformation.module.scss';
import React, { useState } from 'react';
import { useRecoilValue } from 'recoil';
import OrganizationAuthRep from 'rep/OrganizationAuthRep';
import organizationAuthIdState from 'state/core/organizationAuthId';

interface Props {
  auth: OrganizationAuthRep;
  organizationName: string;
}

const UpdateAuthInformation: React.FC<Props> = ({ auth, organizationName }) => {
  const currentAuthId = useRecoilValue(organizationAuthIdState);

  const updateAuth = useUpdateOrganizationAuth(auth.organizationId, auth.id);
  const deleteAuth = useDeleteOrganizationAuth(auth.organizationId, auth.id);

  const fields = new FormFields([
    ['body.auth0OrganizationName', useFormField(auth.auth0OrganizationName)],
  ]);

  const [isDeleting, setIsDeleting] = useState(false);

  const handleSubmit = async () => {
    await updateAuth({
      auth0OrganizationName: fields.getString('body.auth0OrganizationName').value,
    });
  };

  const handleDelete = async () => {
    try {
      await deleteAuth();
    } finally {
      setIsDeleting(false);
    }
  };

  return (
    <>
      <Form fields={fields} onSubmit={handleSubmit}>
        <Container direction="vertical">
          <Auth0OrganizationNameInput />
          <Container direction="horizontal">
            <FormSubmitButton>{'Update'}</FormSubmitButton>
            <Button
              className={styles.deleteButton}
              disabled={auth.id === currentAuthId}
              variant="danger"
              onClick={() => setIsDeleting(true)}
            >
              {'Delete'}
            </Button>
          </Container>
        </Container>
      </Form>
      <DeleteConfirmation
        isOpen={isDeleting}
        organizationName={organizationName}
        onCancel={() => setIsDeleting(false)}
        onDelete={handleDelete}
      />
    </>
  );
};

export default UpdateAuthInformation;
