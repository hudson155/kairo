/* IMPORTANT: Preserve CSS import order. */
import 'react-toastify/dist/ReactToastify.css';

import React, { ReactNode } from 'react';
import { toast as delegate, ToastContainer as Delegate } from 'react-toastify';
import styles from './ToastContainer.module.scss';

interface Toast {
  success(content: ReactNode): void;
  failure(content: ReactNode): void;
}

const toast: Toast = {
  success: (content) => delegate.success(content, { autoClose: 4000 }),
  failure: (content) => delegate.error(content, { autoClose: 7000 }),
};

const ToastContainer: React.FC = () => {
  return (
    <Delegate
      className={styles.container}
      closeOnClick={false}
      newestOnTop={true}
      toastClassName={styles.toast}
    />
  );
};

export default ToastContainer;

export const useToast = (): Toast => toast;
