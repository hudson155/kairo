import ConfirmationPopover from 'component/confirmationPopover/ConfirmationPopover';
import Paragraph from 'component/text/Paragraph';
import Text from 'component/text/Text';
import React from 'react';

interface FooProps {
  isOpen: boolean;
  organizationName: string;
  onCancel: () => void;
  onDelete: () => Promise<void>;
}

const DeleteConfirmation: React.FC<FooProps> = ({ isOpen, organizationName, onCancel, onDelete }) => {
  return (
    <ConfirmationPopover isOpen={isOpen} variant="danger" onCancel={onCancel} onConfirm={onDelete}>
      <Paragraph>
        {'Are you sure you want to delete the auth information for '}
        <Text weight="bold">{organizationName}</Text>
        {'?'}
      </Paragraph>
      <Paragraph>
        {'Users of this organization will be unable to sign in'}
      </Paragraph>
    </ConfirmationPopover>
  );
};

export default DeleteConfirmation;
