import Button from 'component/button/Button';
import Container from 'component/container/Container';
import Paragraph from 'component/text/Paragraph';
import createOrganizationRoute from 'page/adminSettings/createOrganization/createOrganizationRoute';
import styles from 'page/adminSettings/organizationList/OrganizationList.module.scss';
import OrganizationListItem from 'page/adminSettings/organizationList/OrganizationListItem';
import React from 'react';
import { useNavigate } from 'react-router-dom';
import OrganizationRep from 'rep/OrganizationRep';

interface Props {
  organizations: OrganizationRep[];
}

const OrganizationList: React.FC<Props> = ({ organizations }) => {
  const navigate = useNavigate();

  const handleCreateOrganization = () => {
    navigate(createOrganizationRoute.path);
  };

  return (
    <Container direction="vertical">
      <Button variant="primary" onClick={handleCreateOrganization}>{'New organization'}</Button>
      <div className={styles.list}>
        {
          organizations.length > 0
            ? organizations.map((organization) => {
              return <OrganizationListItem key={organization.id} organization={organization} />;
            })
            : <Paragraph>{'No organizations found.'}</Paragraph>
        }
      </div>
    </Container>
  );
};

export default OrganizationList;
