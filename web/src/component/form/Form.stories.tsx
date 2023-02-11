import { ComponentMeta, Story } from '@storybook/react';
import ValidationErrorsError from 'api/ValidationErrorsError';
import { useFormField } from 'component/form/FormField';
import FormFields from 'component/form/FormFields';
import FormSubmitButton from 'component/form/FormSubmitButton';
import FormTextInput from 'component/form/input/FormTextInput';
import InputGroup from 'component/input/group/InputGroup';
import React, { ComponentProps } from 'react';
import * as Decorator from 'story/Decorator';
import Form, { useForm } from './Form';

export default {
  decorators: [Decorator.toastProvider()],
} as ComponentMeta<typeof Form>;

const Template: Story<ComponentProps<typeof Form>> = () => {
  const fields = new FormFields([
    ['body.name', useFormField('Jeff')],
  ]);

  const handleSubmit = (): Promise<void> => {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        if (fields.getString('body.name').value === '') {
          reject(new ValidationErrorsError([{ propertyPath: 'body.name', message: 'You left the name blank!' }]));
          return;
        }
        resolve();
      }, 400);
    });
  };

  return (
    <Form fields={fields} onSubmit={handleSubmit}>
      <InputGroup>
        <Input field="body.name" label="Name" />
        <FormSubmitButton>{'Save'}</FormSubmitButton>
      </InputGroup>
    </Form>
  );
};

export const Default = Template.bind({});

interface Props {
  field: string;
  label: string;
}

const Input: React.FC<Props> = ({ field, label }) => {
  const { fields } = useForm();
  return <FormTextInput field={fields.getString(field)} label={label} />;
};
