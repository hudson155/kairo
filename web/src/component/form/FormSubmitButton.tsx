import Button from 'component/button/Button';
import { useForm } from 'component/form/Form';
import React, { ReactNode } from 'react';
import styles from './FormSubmitButton.module.scss';

interface Props {
  children: ReactNode;
}

const FormSubmitButton: React.FC<Props> = ({ children }) => {
  const { isSubmitting } = useForm();

  return <Button.Submit className={styles.button} isSubmitting={isSubmitting}>{children}</Button.Submit>;
};

export default FormSubmitButton;
