import useDeleteOrganization from 'action/deleteOrganization';
import Button from 'component/button/Button';
import Container from 'component/container/Container';
import Paper from 'component/paper/Paper';
import Section from 'component/section/Section';
import Heading2 from 'component/text/Heading2';
import Paragraph from 'component/text/Paragraph';
import DeleteConfirmation from 'page/adminSettings/editOrganization/deleteOrganization/DeleteConfirmation';
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import OrganizationRep from 'rep/OrganizationRep';
import organizationIdState from 'state/core/organizationId';

interface Props {
  organization: OrganizationRep;
}

const DeleteOrganization: React.FC<Props> = ({ organization }) => {
  const navigate = useNavigate();

  const currentOrganizationId = useRecoilValue(organizationIdState);

  const deleteOrganization = useDeleteOrganization(organization.id);

  const [isDeleting, setIsDeleting] = useState(false);

  const handleDelete = async () => {
    try {
      await deleteOrganization();
    } finally {
      setIsDeleting(false);
    }
    navigate('..');
  };

  return (
    <Section>
      <Paper variant="danger">
        <Container direction="vertical">
          <Heading2>{'Delete organization'}</Heading2>
          <Paragraph>{'Deleting the organization will REMOVE ALL DATA associated with it.'}</Paragraph>
          <Button
            disabled={organization.id === currentOrganizationId}
            variant="danger"
            onClick={() => setIsDeleting(true)}
          >
            {'Delete'}
          </Button>
          <DeleteConfirmation
            isOpen={isDeleting}
            organizationName={organization.name}
            onCancel={() => setIsDeleting(false)}
            onDelete={handleDelete}
          />
        </Container>
      </Paper>
    </Section>
  );
};

export default DeleteOrganization;
