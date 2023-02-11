import { useState } from 'react';

export default interface FormField {
  value: string;
  setValue: (newValue: string) => void;
  errorMessage: string | undefined;
  setErrorMessage: (errorMessage: string | undefined) => void;
}

export const useFormField = (initialValue: string): FormField => {
  const [value, setValue] = useState(initialValue);
  const [errorMessage, setErrorMessage] = useState<string>();
  return { value, setValue, errorMessage, setErrorMessage };
};
