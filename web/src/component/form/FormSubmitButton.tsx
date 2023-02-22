import Button from 'component/button/Button';
import { useForm } from 'component/form/Form';
import styles from 'component/form/FormSubmitButton.module.scss';
import React, { ReactNode } from 'react';

interface Props {
  children: ReactNode;
}

const FormSubmitButton: React.FC<Props> = ({ children }) => {
  const { isSubmitting } = useForm();

  return <Button.Submit className={styles.button} isSubmitting={isSubmitting}>{children}</Button.Submit>;
};

export default FormSubmitButton;
