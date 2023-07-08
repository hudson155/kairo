import { Popover, Transition } from '@headlessui/react';
import classNames from 'classnames';
import Button from 'component/button/Button';
import styles from 'component/confirmationPopover/ConfirmationPopover.module.scss';
import Container from 'component/container/Container';
import Form from 'component/form/Form';
import FormSubmitButton from 'component/form/submitButton/SubmitButton';
import React, { Fragment, ReactNode } from 'react';
import { transitions } from 'style/transitions';

type Variant = 'danger';

interface Props {
  isOpen: boolean;
  variant: Variant;
  children: ReactNode;
  onCancel: () => void;
  onConfirm: () => void | Promise<void>;
}

const ConfirmationPopover: React.FC<Props> = ({ isOpen, variant, children, onCancel, onConfirm }) => {
  return (
    <Transition as={Fragment} show={isOpen}>
      <Popover className={styles.popover}>
        <Transition.Child as={Fragment} {...transitions('fade')}>
          <Popover.Overlay className={classNames(styles.backdrop, variantClassName(variant))} />
        </Transition.Child>
        <Transition.Child as={Fragment} {...transitions('fade', 'zoom')}>
          <Popover.Panel className={styles.container}>
            <Form onSubmit={onConfirm}>
              <Container className={styles.panelContainer} direction="vertical">
                {children}
                <Container className={styles.buttonContainer} direction="horizontal">
                  <FormSubmitButton variant={variant}>{'Delete'}</FormSubmitButton>
                  <Popover.Button as={Fragment}>
                    <Button variant="secondary" onClick={onCancel}>{'Cancel'}</Button>
                  </Popover.Button>
                </Container>
              </Container>
            </Form>
          </Popover.Panel>
        </Transition.Child>
      </Popover>
    </Transition>
  );
};

export default ConfirmationPopover;

const variantClassName = (variant: Variant): string => {
  switch (variant) {
  case 'danger':
    return styles.danger;
  }
};
