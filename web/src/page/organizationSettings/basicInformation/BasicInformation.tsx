import useUpdateOrganization from 'action/updateOrganization';
import Container from 'component/container/Container';
import Form from 'component/form/Form';
import { useFormField } from 'component/form/FormField';
import FormFields from 'component/form/FormFields';
import SubmitButton from 'component/form/submitButton/SubmitButton';
import Heading2 from 'component/text/Heading2';
import OrganizationNameInput from 'page/organizationSettings/basicInformation/organizationName/OrganizationNameInput';
import React from 'react';
import { useRecoilValue } from 'recoil';
import organizationNameState from 'state/global/core/organizationName';

const BasicInformation: React.FC = () => {
  const updateOrganization = useUpdateOrganization();

  const fields = new FormFields([
    ['body.name', useFormField(useRecoilValue(organizationNameState))],
  ]);

  const handleSubmit = async () => {
    await updateOrganization({
      name: fields.getString('body.name').value,
    });
  };

  return (
    <>
      <Heading2>{'Basic information'}</Heading2>
      <Form fields={fields} onSubmit={handleSubmit}>
        <Container direction="vertical">
          <OrganizationNameInput />
          <SubmitButton>{'Save'}</SubmitButton>
        </Container>
      </Form>
    </>
  );
};

export default BasicInformation;
