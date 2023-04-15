import { useForm } from 'component/form/Form';
import SubmitButtonDelegate, { Props as DelegateProps } from 'component/form/submitButton/SubmitButtonDelegate';
import React from 'react';

type Props = Omit<DelegateProps, 'isSubmitting'>;

const SubmitButton: React.ForwardRefRenderFunction<HTMLButtonElement, Props> =
  ({ ...props }, ref) => {
    const { isSubmitting } = useForm();

    return <SubmitButtonDelegate ref={ref} isSubmitting={isSubmitting} {...props} />;
  };

export default React.forwardRef(SubmitButton);
