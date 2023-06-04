import useCreateOrganization from 'action/createOrganization';
import Container from 'component/container/Container';
import Form from 'component/form/Form';
import { useFormField } from 'component/form/FormField';
import FormFields from 'component/form/FormFields';
import FormSubmitButton from 'component/form/submitButton/SubmitButton';
import Paper from 'component/paper/Paper';
import Section from 'component/section/Section';
import Heading2 from 'component/text/Heading2';
import OrganizationNameInput from 'page/organizationSettings/basicInformation/OrganizationNameInput';
import React from 'react';
import { useNavigate } from 'react-router-dom';

const BasicInformation: React.FC = () => {
  const navigate = useNavigate();

  const createOrganization = useCreateOrganization();

  const fields = new FormFields([
    ['body.name', useFormField('')],
  ]);

  const handleSubmit = async () => {
    const { id: organizationId } = await createOrganization({
      name: fields.getString('body.name').value,
    });
    navigate(`../${organizationId}`);
  };

  return (
    <Section>
      <Paper>
        <Container direction="vertical">
          <Heading2>{'Basic information'}</Heading2>
          <Form fields={fields} onSubmit={handleSubmit}>
            <Container direction="vertical">
              <OrganizationNameInput />
              <FormSubmitButton>{'Create'}</FormSubmitButton>
            </Container>
          </Form>
        </Container>
      </Paper>
    </Section>
  );
};

export default BasicInformation;
