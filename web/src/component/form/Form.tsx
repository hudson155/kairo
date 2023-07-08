import ValidationErrorsError from 'api/ValidationErrorsError';
import FormFields from 'component/form/FormFields';
import { useToast } from 'component/toast/ToastContainer';
import UserError from 'error/UserError';
import React, { FormEvent, ReactNode, useContext, useMemo, useState } from 'react';

export interface FormContext {
  fields: FormFields;
  isSubmitting: boolean;
}

const context = React.createContext<FormContext>(
  undefined as unknown as FormContext,
);

interface Props {
  fields?: FormFields;
  onSubmit: () => void | Promise<void>;
  children: ReactNode;
}

/**
 * Renders an HTML form element, and also helps manage form state.
 * To use this, create some form fields and a handleSubmit function at the root,
 * and access the fields using useForm().
 */
const Form: React.FC<Props> = ({ fields = new FormFields(), onSubmit, children }) => {
  const toast = useToast();

  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault(); // Prevent form default behaviour.

    if (isSubmitting) {
      // Prevent double-submission.
      return;
    }

    setIsSubmitting(true);

    void (async () => {
      try {
        await onSubmit();
        handleSuccess();
      } catch (e) {
        handleFailure(e);
      } finally {
        setIsSubmitting(false);
      }
    })();
  };

  const handleSuccess = () => {
    toast.success('Success!');

    for (const field of fields.values()) {
      field.setErrorMessage(undefined);
    }
  };

  const handleFailure = (e: unknown) => {
    if (e instanceof UserError) {
      console.warn(e);
      toast.failure(e.message);
      return;
    }

    if (e instanceof ValidationErrorsError) {
      console.warn(e);
      toast.failure('Please review the errors.');
      for (const error of e.errors) {
        const field = fields.get(error.propertyPath);
        if (!field) continue;
        field.setErrorMessage(error.message);
      }
      return;
    }

    console.error(e);
    toast.failure(toast.error.generic);
    throw e;
  };

  const value: FormContext = useMemo(() => ({ fields, isSubmitting }), [fields, isSubmitting]);

  return (
    <context.Provider value={value}>
      <form onSubmit={handleSubmit}>
        {children}
      </form>
    </context.Provider>
  );
};

export default Form;

export const useForm = (): FormContext => {
  return useContext(context);
};
