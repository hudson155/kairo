import FormField from 'component/form/FormField';
import TextInput, { Props as TextInputProps } from 'component/input/TextInput';
import React from 'react';

type Props = Omit<TextInputProps, 'errorMessage' | 'value' | 'onChange'> & { field: FormField };

const FormTextInput: React.FC<Props> = ({ field, ...props }) => {
  return (
    <TextInput
      errorMessage={field.errorMessage}
      value={field.value}
      onChange={field.setValue}
      {...props}
    />
  );
};

export default FormTextInput;
