import { Popover } from '@headlessui/react';
import Button from 'component/button/Button';
import Container from 'component/container/Container';
import Form from 'component/form/Form';
import FormSubmitButton from 'component/form/submitButton/SubmitButton';
import Paragraph from 'component/text/Paragraph';
import Text from 'component/text/Text';
import styles from 'page/adminSettings/editOrganization/authInformation/DeleteConfirmation.module.scss';
import React, { Fragment } from 'react';

interface FooProps {
  organizationName: string;
  onCancel: () => void;
  onDelete: () => Promise<void>;
}

const DeleteConfirmation: React.FC<FooProps> = ({ organizationName, onCancel, onDelete }) => {
  return (
    <Popover as={Fragment}>
      <Popover.Overlay className={styles.backdrop} static={true} />
      <Popover.Panel className={styles.container} static={true}>
        <Form onSubmit={onDelete}>
          <Container className={styles.panelContainer} direction="vertical">
            <Paragraph>
              {'Are you sure you want to delete the auth information for '}
              <Text weight="bold">{organizationName}</Text>
              {'?'}
            </Paragraph>
            <Paragraph>
              {'Users of this organization will be unable to sign in'}
            </Paragraph>
            <Container className={styles.buttonContainer} direction="horizontal">
              <FormSubmitButton variant="danger">{'Delete'}</FormSubmitButton>
              <Popover.Button as={Fragment}>
                <Button variant="secondary" onClick={onCancel}>{'Cancel'}</Button>
              </Popover.Button>
            </Container>
          </Container>
        </Form>
      </Popover.Panel>
    </Popover>
  );
};

export default DeleteConfirmation;
