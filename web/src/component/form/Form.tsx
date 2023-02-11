import ValidationErrorsError from 'api/ValidationErrorsError';
import FormFields from 'component/form/FormFields';
import { useToast } from 'component/toast/ToastContainer';
import React, { FormEventHandler, ReactNode, useContext, useMemo, useState } from 'react';

export interface FormContext {
  fields: FormFields;
  isSubmitting: boolean;
}

// eslint-disable-next-line @typescript-eslint/naming-convention
export const Context = React.createContext<FormContext>(undefined as unknown as FormContext);

interface Props {
  fields: FormFields;
  onSubmit: () => Promise<void>;
  children: ReactNode;
}

/**
 * Renders an HTML form element, and also helps manage form state.
 * To use this, create some form fields and a handleSubmit function at the root,
 * and access the fields using useForm().
 */
const Form: React.FC<Props> = ({ fields, onSubmit, children }) => {
  const toast = useToast();

  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleSubmit: FormEventHandler<HTMLFormElement> = (event) => {
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
    console.error(e);

    if (e instanceof ValidationErrorsError) {
      toast.failure('Please review the errors.');
      for (const error of e.errors) {
        const field = fields.get(error.propertyPath);
        if (!field) continue;
        field.setErrorMessage(error.message);
      }
      return;
    }

    toast.failure('Something went wrong! Please try again.');
    throw e;
  };

  const value: FormContext = useMemo(() => ({ fields, isSubmitting }), [fields, isSubmitting]);

  return (
    <Context.Provider value={value}>
      <form onSubmit={handleSubmit}>
        {children}
      </form>
    </Context.Provider>
  );
};

export default Form;

export const useForm = (): FormContext => {
  return useContext(Context);
};
