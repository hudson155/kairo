import { Meta, Story } from '@storybook/react';
import ValidationErrorsError from 'api/ValidationErrorsError';
import Container from 'component/container/Container';
import Form, { useForm } from 'component/form/Form';
import { useFormField } from 'component/form/FormField';
import FormFields from 'component/form/FormFields';
import FormTextInput from 'component/form/input/FormTextInput';
import SubmitButton from 'component/form/submitButton/SubmitButton';
import React, { ComponentProps } from 'react';
import * as Decorator from 'story/Decorator';

type StoryProps = ComponentProps<typeof Form>;

const story: Meta<StoryProps> = {
  decorators: [Decorator.toastProvider()],
};

export default story;

const Template: Story<StoryProps> = () => {
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
      <Container direction="vertical">
        <Input field="body.name" label="Name" placeholder="John Smith" />
        <SubmitButton>{'Save'}</SubmitButton>
      </Container>
    </Form>
  );
};

export const Default = Template.bind({});

interface Props {
  field: string;
  label: string;
  placeholder?: string;
}

const Input: React.FC<Props> = ({ field, label, placeholder = undefined }) => {
  const { fields } = useForm();
  return <FormTextInput field={fields.getString(field)} label={label} placeholder={placeholder} />;
};
