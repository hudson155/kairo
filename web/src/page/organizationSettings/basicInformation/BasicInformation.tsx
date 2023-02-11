import useUpdateOrganization from 'action/updateOrganization';
import Form from 'component/form/Form';
import { useFormField } from 'component/form/FormField';
import FormFields from 'component/form/FormFields';
import FormSubmitButton from 'component/form/FormSubmitButton';
import InputGroup from 'component/input/group/InputGroup';
import Heading2 from 'component/text/Heading2';
import OrganizationNameInput from 'page/organizationSettings/basicInformation/organizationName/OrganizationNameInput';
import React from 'react';
import { useRecoilValue } from 'recoil';
import organizationNameState from 'state/core/organizationName';

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
        <InputGroup>
          <OrganizationNameInput />
          <FormSubmitButton>{'Save'}</FormSubmitButton>
        </InputGroup>
      </Form>
    </>
  );
};

export default BasicInformation;
