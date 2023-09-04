import useUpdateOrganization from 'action/updateOrganization';
import Container from 'component/container/Container';
import Form from 'component/form/Form';
import { useFormField } from 'component/form/FormField';
import FormFields from 'component/form/FormFields';
import FormSubmitButton from 'component/form/submitButton/SubmitButton';
import Paper from 'component/paper/Paper';
import Section from 'component/section/Section';
import Heading2 from 'component/text/Heading2';
import OrganizationIdInput from 'page/organizationSettings/basicInformation/OrganizationIdInput';
import OrganizationNameInput from 'page/organizationSettings/basicInformation/OrganizationNameInput';
import React from 'react';
import OrganizationRep from 'rep/OrganizationRep';

interface Props {
  organization: OrganizationRep;
}

const BasicInformation: React.FC<Props> = ({ organization }) => {
  const updateOrganization = useUpdateOrganization(organization.id);

  const fields = new FormFields([
    ['body.name', useFormField(organization.name)],
  ]);

  const handleSubmit = async () => {
    await updateOrganization({
      name: fields.getString('body.name').value,
    });
  };

  return (
    <Section>
      <Paper>
        <Container direction="vertical">
          <Heading2>{'Basic information'}</Heading2>
          <Form fields={fields} onSubmit={handleSubmit}>
            <Container direction="vertical">
              <OrganizationIdInput value={organization.id} />
              <OrganizationNameInput />
              <FormSubmitButton>{'Update'}</FormSubmitButton>
            </Container>
          </Form>
        </Container>
      </Paper>
    </Section>
  );
};

export default BasicInformation;
